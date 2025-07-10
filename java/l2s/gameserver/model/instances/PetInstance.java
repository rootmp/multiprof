package l2s.gameserver.model.instances;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dao.JdbcEntityState;
import l2s.commons.dbutils.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.data.xml.holder.PetDataHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.MySqlDataInsert;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.actor.basestats.PetBaseStats;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.base.MountType;
import l2s.gameserver.model.base.PetType;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PetInventory;
import l2s.gameserver.model.items.PetItemInfo;
import l2s.gameserver.model.items.attachment.FlagItemAttachment;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExChangeNPCState;
import l2s.gameserver.network.l2.s2c.InventoryUpdatePacket;
import l2s.gameserver.network.l2.s2c.SocialActionPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.TimeStamp;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.WeaponTemplate;
import l2s.gameserver.templates.item.data.RewardItemData;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.templates.pet.PetData;
import l2s.gameserver.templates.pet.PetSkillData;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @offlike completed by Bonux
 **/
public class PetInstance extends Servitor
{
	private static final Logger _log = LoggerFactory.getLogger(PetInstance.class);

	private final static int BASE_CORPSE_TIME = 86400;

	private final int _controlItemObjId;
	private int _currentFeed;
	private Future<?> _feedTask;
	protected PetInventory _inventory;
	private int _level;
	private boolean _respawned;
	private int lostExp;
	private final PetData _data;
	private int _npcState;

	private final int _corpseTime;
	
	private int nameId = -1;
	private int statSkillId = 0;
	private int statSkillLevel = 0;

	public static PetInstance restore(ItemInstance control, NpcTemplate template, Player owner)
	{
		PetInstance pet = null;

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT objId, name, curHp, curMp, exp, sp, fed FROM pets WHERE item_obj_id=?");
			statement.setInt(1, control.getObjectId());
			rset = statement.executeQuery();

			if (!rset.next())
			{
				if (PetDataHolder.isBabyPet(template.getId()) || PetDataHolder.isImprovedBabyPet(template.getId()) || PetDataHolder.isSpecialPet(template.getId()))
					pet = new PetBabyInstance(IdFactory.getInstance().getNextId(), template, owner, control);
				else
					pet = new PetInstance(IdFactory.getInstance().getNextId(), template, owner, control);
				return pet;
			}

			if (PetDataHolder.isBabyPet(template.getId()) || PetDataHolder.isImprovedBabyPet(template.getId()) || PetDataHolder.isSpecialPet(template.getId()))
				pet = new PetBabyInstance(rset.getInt("objId"), template, owner, control, rset.getLong("exp"));
			else
				pet = new PetInstance(rset.getInt("objId"), template, owner, control, rset.getLong("exp"));

			pet.setRespawned(true);

			String name = rset.getString("name");
			pet.setName(name == null || name.isEmpty() ? StringUtils.EMPTY : name);
			pet.setCurrentHpMp(rset.getDouble("curHp"), rset.getInt("curMp"), true);
			pet.setCurrentCp(pet.getMaxCp());
			pet.setSp(rset.getInt("sp"));
			pet.setCurrentFed(rset.getInt("fed"), false);
		}
		catch (Exception e)
		{
			_log.error("Could not restore Pet data from item: " + control + "!", e);
			return null;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return pet;
	}

	/**
	 * Создание нового пета
	 */
	public PetInstance(int objectId, NpcTemplate template, Player owner, ItemInstance control)
	{
		this(objectId, template, owner, control, 0);
	}

	/**
	 * Загрузка уже существующего пета
	 */
	public PetInstance(int objectId, NpcTemplate template, Player owner, ItemInstance control, long exp)
	{
		super(objectId, template, owner);

		_data = PetDataHolder.getInstance().getTemplateByNpcId(template.getId());
		_controlItemObjId = control.getObjectId();
		_exp = exp;
		_level = control.getEnchantLevel();

		if (_level <= 0)
		{
			_level = template.level;
			_exp = getExpForThisLevel();
		}

		int minLevel = _data.getMinLvl();
		if (_level < minLevel)
			_level = minLevel;

		if (_exp < getExpForThisLevel())
			_exp = getExpForThisLevel();

		while (_exp >= getExpForNextLevel() && _level < Experience.getMaxLevel())
			_level++;

		while (_exp < getExpForThisLevel() && _level > minLevel)
			_level--;

		if (_data.isOfType(PetType.KARMA) || _data.isOfType(PetType.SPECIAL))
		{
			_level = owner.getLevel();
			_exp = getExpForNextLevel();
		}

		_inventory = new PetInventory(this);

		_corpseTime = template.getAIParams().getInteger(NpcInstance.CORPSE_TIME, BASE_CORPSE_TIME);

		refreshPetSkills();
	}

	public PetData getData()
	{
		return _data;
	}

	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		startFeedTask();
	}

	@Override
	protected void onDespawn()
	{
		super.onDespawn();

		stopFeedTask();
	}

	private void tryFeed()
	{
		ItemInstance food = null;
		for (int foodId : getFoodId())
		{
			food = getInventory().getItemByItemId(foodId);
			if (food != null)
			{
				if (food.getTemplate().useItem(this, food, false, false))
				{
					getPlayer().sendPacket(new SystemMessagePacket(SystemMsg.YOUR_PET_WAS_HUNGRY_SO_IT_ATE_S1).addItemName(food.getItemId()));
					if (Rnd.chance(5))
						getPlayer().sendPacket(SystemMsg.YOUR_PET_ATE_A_LITTLE_BUT_IS_STILL_HUNGRY);
					break;
				}
				food = null;
			}
		}
	}

	@Override
	public void addExpAndSp(long addToExp, long addToSp)
	{
		Player owner = getPlayer();

		if (isHungry()) // Проверить, будет ли отниматся в данной ситуации у хозяина часть опыта,
						// которая призначалась пету.
			return;

		if (getData().isOfType(PetType.SPECIAL))
			return;

		_exp += addToExp;
		_sp += addToSp;

		if (_exp > getMaxExp())
			_exp = getMaxExp();

		if (addToExp > 0 || addToSp > 0)
			owner.sendPacket(new SystemMessage(SystemMessage.THE_PET_ACQUIRED_EXPERIENCE_POINTS_OF_S1).addNumber(addToExp));

		int old_level = _level;

		while (_exp >= getExpForNextLevel() && _level < Experience.getMaxLevel())
			_level++;

		while (_exp < getExpForThisLevel() && _level > getMinLevel())
			_level--;

		if (old_level < _level)
		{
			owner.sendMessage(new CustomMessage("l2s.gameserver.model.instances.L2PetInstance.PetLevelUp").addNumber(_level));
			broadcastPacket(new SocialActionPacket(getObjectId(), SocialActionPacket.LEVEL_UP));
			setCurrentHpMp(getMaxHp(), getMaxMp());
		}

		if (old_level != _level)
		{
			updateControlItem();
			refreshPetSkills();
		}

		if (addToExp > 0 || addToSp > 0)
			sendStatusUpdate();
	}

	@Override
	public boolean consumeItem(int itemConsumeId, long itemCount, boolean sendMessage)
	{
		return getInventory().destroyItemByItemId(itemConsumeId, itemCount);
	}

	private void deathPenalty()
	{
		if (isInZoneBattle())
			return;

		int lvl = getLevel();
		double percentLost = -0.07 * lvl + 6.5;
		// Calculate the Experience loss
		lostExp = (int) Math.round((getExpForNextLevel() - getExpForThisLevel()) * percentLost / 100);
		addExpAndSp(-lostExp, 0);
	}

	/**
	 * Remove the Pet from DB and its associated item from the player inventory
	 */
	private void destroyControlItem()
	{
		if (getControlItemObjId() == 0)
			return;

		if (!getPlayer().getInventory().destroyItemByObjectId(getControlItemObjId(), 1L))
			return;

		// pet control item no longer exists, delete the pet from the db
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?");
			statement.setInt(1, getControlItemObjId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("could not delete pet:" + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);

		getPlayer().sendPacket(SystemMsg.THE_PET_HAS_BEEN_KILLED);

		if (getData().isOfType(PetType.SPECIAL))
			return;

		stopFeedTask();
		deathPenalty();
	}

	@Override
	public void doPickupItem(GameObject object)
	{
		Player owner = getPlayer();

		getMovement().stopMove();

		if (!object.isItem())
			return;

		ItemInstance item = (ItemInstance) object;

		synchronized (item)
		{
			if (!item.isVisible())
				return;

			if (item.isHerb())
			{
				for (SkillEntry skillEntry : item.getTemplate().getAttachedSkills())
					altUseSkill(skillEntry, this);
				item.deleteMe();
				return;
			}

			if (!getInventory().validateWeight(item))
			{
				sendPacket(SystemMsg.YOUR_PET_CANNOT_CARRY_ANY_MORE_ITEMS_);
				return;
			}

			if (!getInventory().validateCapacity(item))
			{
				sendPacket(SystemMsg.YOUR_PET_CANNOT_CARRY_ANY_MORE_ITEMS);
				return;
			}

			if (!item.getTemplate().getHandler().pickupItem(this, item))
				return;

			FlagItemAttachment attachment = item.getAttachment() instanceof FlagItemAttachment ? (FlagItemAttachment) item.getAttachment() : null;
			if (attachment != null)
				return;

			item.pickupMe();
		}

		if (owner.getParty() == null || owner.getParty().getLootDistribution() == Party.ITEM_LOOTER)
		{
			getInventory().addItem(item);
			sendChanges();
		}
		else
			owner.getParty().distributeItem(owner, item, null);
		broadcastPickUpMsg(item);
	}

	public void doRevive(double percent)
	{
		restoreExp(percent);
		doRevive();
	}

	@Override
	public void doRevive()
	{
		stopDecay();
		super.doRevive();
		startFeedTask();
		setRunning();
	}

	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return null;
	}

	@Override
	public WeaponTemplate getActiveWeaponTemplate()
	{
		return null;
	}

	public ItemInstance getControlItem()
	{
		Player owner = getPlayer();
		if (owner == null)
			return null;
		int item_obj_id = getControlItemObjId();
		if (item_obj_id == 0)
			return null;
		return owner.getInventory().getItemByObjectId(item_obj_id);
	}

	@Override
	public int getControlItemObjId()
	{
		return _controlItemObjId;
	}

	@Override
	public int getCurrentFed()
	{
		if (Config.ALT_PETS_NOT_STARVING)
			return getMaxFed();
		return _currentFeed;
	}

	@Override
	public long getExpForNextLevel()
	{
		return _data.getExp(_level + 1);
	}

	@Override
	public long getExpForThisLevel()
	{
		return _data.getExp(_level);
	}

	public int[] getFoodId()
	{
		return _data.getFood(_level);
	}

	@Override
	public PetInventory getInventory()
	{
		return _inventory;
	}

	@Override
	public long getWearedMask()
	{
		return _inventory.getWearedMask();
	}

	@Override
	public final int getLevel()
	{
		return _level;
	}

	public void setLevel(int level)
	{
		_level = level;
	}

	public int getMinLevel()
	{
		return _data.getMinLvl();
	}

	public long getMaxExp()
	{
		return _data.getExp(_data.getMaxLvl());
	}

	@Override
	public int getMaxFed()
	{
		return _data.getMaxMeal(_level);
	}

	@Override
	public int getMaxLoad()
	{
		return (int) getStat().calc(Stats.MAX_LOAD, _data.getMaxLoad(_level), null, null);
	}

	@Override
	public int getInventoryLimit()
	{
		return Config.ALT_PET_INVENTORY_LIMIT;
	}

	@Override
	public int getSoulshotConsumeCount()
	{
		return _data.getSoulshotCount(_level);
	}

	@Override
	public int getSpiritshotConsumeCount()
	{
		return _data.getSpiritshotCount(_level);
	}

	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}

	@Override
	public WeaponTemplate getSecondaryWeaponTemplate()
	{
		return null;
	}

	public int getSkillLevel(int skillId)
	{
		if (_skills == null || _skills.get(skillId) == null)
			return -1;
		int lvl = getLevel();
		return lvl > 70 ? 7 + (lvl - 70) / 5 : lvl / 10;
	}

	@Override
	public int getServitorType()
	{
		return 2;
	}

	@Override
	public boolean isMountable()
	{
		return getData().getMountType() != MountType.NONE;
	}

	public boolean isMyFeed(int itemId)
	{
		return ArrayUtils.contains(getFoodId(), itemId);
	}

	public boolean isRespawned()
	{
		return _respawned;
	}

	public void restoreExp(double percent)
	{
		if (lostExp != 0)
		{
			addExpAndSp((long) (lostExp * percent / 100.), 0);
			lostExp = 0;
		}
	}

	public void setCurrentFed(int num, boolean send)
	{
		_currentFeed = Math.min(getMaxFed(), Math.max(0, num));
		setNpcState(getCurrentFed() <= 0 ? 100 : 101, send);
	}

	public void setRespawned(boolean respawned)
	{
		_respawned = respawned;
	}

	@Override
	public void setSp(int sp)
	{
		_sp = sp;
	}

	private void startFeedTask()
	{
		if (isDead())
			return;

		if (_feedTask != null)
			return;

		if (Config.ALT_PETS_NOT_STARVING)
			return;

		_feedTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new FeedTask(), 10000L, 10000L);
	}

	private void stopFeedTask()
	{
		if (_feedTask != null)
		{
			_feedTask.cancel(false);
			_feedTask = null;
		}
	}

	private void consumeMeal()
	{
		int mealConsume = isInCombat() ? _data.getBattleMealConsume(_level) : _data.getNormalMealConsume(_level);
		setCurrentFed(getCurrentFed() - mealConsume, true);
		sendStatusUpdate();
	}

	public final String selectPetSkills = "SELECT * FROM petsSkills WHERE hashId=?";
	public final String insertPetSkills = "INSERT INTO petsSkills (skillId, skillLevel, petId, hashId) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE skillId=VALUES(skillId), skilllevel=VALUES(skilllevel), petId=VALUES(petId)";
	public final String deletePetSkills = "DELETE FROM petsSkills WHERE hashId=?";

	public void storePetSkills(int skillId, int skillLevel, int petId)
	{
		try (PreparedStatement ps = DatabaseFactory.getInstance().getConnection().prepareStatement(insertPetSkills))
		{
			ps.setInt(1, skillId);
			ps.setInt(2, skillLevel);
			ps.setInt(3, petId);
			ps.setLong(4, getPlayer().getObjectId() * 100000L + getControlItem().getItemId());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			_log.warn("failed to update evolved pets ", e);
		}
	}

	public void deletePetSkills()
	{
		MySqlDataInsert.set(deletePetSkills, getPlayer().getObjectId() * 100000L + getControlItem().getItemId());
	}

	@Override
	protected void onAddSkill(SkillEntry skill)
	{
		super.onAddSkill(skill);
	}

	@Override
	protected void onRemoveSkill(SkillEntry skillEntry)
	{
		super.onRemoveSkill(skillEntry);
	}

	public void store()
	{
		if (getControlItemObjId() == 0 || _exp == 0)
			return;

		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			String req;
			if (!isRespawned())
				req = "INSERT INTO pets (name,curHp,curMp,exp,sp,fed,objId,item_obj_id) VALUES (?,?,?,?,?,?,?,?)";
			else
				req = "UPDATE pets SET name=?,curHp=?,curMp=?,exp=?,sp=?,fed=?,objId=? WHERE item_obj_id = ?";
			statement = con.prepareStatement(req);
			statement.setString(1, getName().equalsIgnoreCase(getTemplate().name) ? "" : getName());
			statement.setDouble(2, getCurrentHp());
			statement.setDouble(3, getCurrentMp());
			statement.setLong(4, _exp);
			statement.setLong(5, _sp);
			statement.setInt(6, getCurrentFed());
			statement.setInt(7, getObjectId());
			statement.setInt(8, _controlItemObjId);
			statement.executeUpdate();
		}
		catch (Exception e)
		{
			_log.error("Could not store pet data!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}

		_respawned = true;
	}

	@Override
	protected void onDecay()
	{
		getInventory().store();
		destroyControlItem(); // this should also delete the pet from the db

		super.onDecay();
	}

	@Override
	public void unSummon(boolean logout)
	{
		stopFeedTask();
		getInventory().store();
		store();

		super.unSummon(logout);
	}

	public void updateControlItem()
	{
		ItemInstance controlItem = getControlItem();
		if (controlItem == null)
			return;
		PetItemInfo petItemInfo = controlItem.getPetInfo();
		if (petItemInfo == null)
		{
			petItemInfo = new PetItemInfo(getEvolveStep(), getNameId(), getStatSkillId(), getStatSkillLevel(), getPetIndex(), getExp());
			controlItem.setPetInfo(petItemInfo);
		}
		else
		{
			petItemInfo.setEvolveStep(getEvolveStep());
			petItemInfo.setNameId(getNameId());
			petItemInfo.setStatSkillId(getStatSkillId());
			petItemInfo.setStatSkillLevel(getStatSkillLevel());
			petItemInfo.setPetIndex(getPetIndex());
			petItemInfo.setExp(getExp());
		}
		controlItem.setEnchantLevel(_level);
		controlItem.setCustomType2(isDefaultName() ? 0 : 1);
		controlItem.setJdbcState(JdbcEntityState.UPDATED);
		controlItem.update();
		Player player = getPlayer();
		player.sendPacket(new InventoryUpdatePacket().addModifiedItem(player, controlItem));
	}

	public int getPetIndex()
	{
		return _data.getIndex();
	}

	private int getStatSkillLevel()
	{
		return this.statSkillLevel;
	}

	private int getStatSkillId()
	{
		return this.statSkillId;
	}

	private int getNameId()
	{
		return this.nameId;
	}

	private int getEvolveStep()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getExpPenalty()
	{
		return (100. - _data.getExpType(_level)) * 0.01;
	}

	@Override
	public int getFormId()
	{
		return _data.getFormId(_level);
	}

	@Override
	public boolean isPet()
	{
		return true;
	}

	public boolean isDefaultName()
	{
		return StringUtils.isEmpty(_name) || getName().equalsIgnoreCase(getTemplate().name);
	}

	@Override
	public boolean isHungry()
	{
		return getCurrentFed() < (int) (getMaxFed() * 0.01 * _data.getHungryLimit(getLevel()));
	}

	@Override
	public int getEffectIdentifier()
	{
		return getObjectId();
	}

	@Override
	public boolean useItem(ItemInstance item, boolean ctrl, boolean sendMsg)
	{
		if (!_isUsingItem.compareAndSet(false, true))
			return false;

		try
		{
			if (isAlikeDead() || isDead() || isOutOfControl())
				return false;

			ItemTemplate template = item.getTemplate();

			if (template.useItem(this, item, ctrl, true))
				return true;

			if (!item.isEquipped() && !template.testCondition(this, item, false))
			{
				if (sendMsg)
					getPlayer().sendPacket(SystemMsg.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
				return false;
			}

			if (isSharedGroupDisabled(template.getReuseGroup()))
				return false;

			if (getPlayer().getInventory().isLockedItem(item)) // TODO: [Bonux] проверить.
				return false;

			if (template.useItem(this, item, ctrl, false))
			{
				long nextTimeUse = template.getReuseType().next(item);
				if (nextTimeUse > System.currentTimeMillis())
				{
					TimeStamp timeStamp = new TimeStamp(item.getItemId(), nextTimeUse, template.getReuseDelay());
					addSharedGroupReuse(template.getReuseGroup(), timeStamp);
				}
				return true;
			}
		}
		finally
		{
			_isUsingItem.set(false);
		}
		return false;
	}

	@Override
	public boolean isNotControlled()
	{
		int lvlDiff = getLevel() - getPlayer().getLevel();
		if (lvlDiff >= 20)
			return true;

		if (isHungry() && getCurrentFed() < (int) (getMaxFed() * 0.10))
			return true;

		return false;
	}

	@Override
	public int getCurrentLoad()
	{
		return getInventory().getTotalWeight();
	}

	@Override
	public int getWeightPenalty()
	{
		double weightproc = (getCurrentLoad() - getStat().calc(Stats.MAX_NO_PENALTY_LOAD, 0, this, null)) / getMaxLoad();
		if (weightproc >= 50.0)
			return 1;
		else if (weightproc >= 60.0)
			return 2;
		else if (weightproc >= 80.0)
			return 3;
		return 0;
	}

	public void setNpcState(int val, boolean send)
	{
		if (_npcState != val)
		{
			if (send)
			{
				ExChangeNPCState packet = new ExChangeNPCState(getObjectId(), val);
				getPlayer().sendPacket(packet);
				getPlayer().broadcastPacket(packet);
			}
			_npcState = val;
		}
	}

	@Override
	public int getNpcState()
	{
		return _npcState;
	}

	@Override
	public PetBaseStats getBaseStats()
	{
		if (_baseStats == null)
			_baseStats = new PetBaseStats(this);
		return (PetBaseStats) _baseStats;
	}

	@Override
	protected int getCorpseTime()
	{
		return _corpseTime;
	}

	private void rewardOwner()
	{
		for (RewardItemData ci : getData().getExpirationRewardItems())
		{
			if (Rnd.chance(ci.getChance()))
			{
				long count;
				long minCount = ci.getMinCount();
				long maxCount = ci.getMaxCount();
				if (minCount == maxCount)
					count = minCount;
				else
					count = Rnd.get(minCount, maxCount);
				ItemFunctions.addItem(getPlayer(), ci.getId(), count, true);
			}
		}
	}

	private void refreshPetSkills()
	{
		final List<PetSkillData> petSkills = new ArrayList<>();

		try (PreparedStatement ps = DatabaseFactory.getInstance().getConnection().prepareStatement(selectPetSkills))
		{
			ps.setLong(1, getPlayer().getObjectId() * 100000L + getControlItem().getItemId());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				int petId = rs.getInt("petId");
				int skillId = rs.getInt("skillId");
				int skillLevel = rs.getInt("skillLevel");
				if (petId == getNpcId())
				{
					petSkills.add(new PetSkillData(skillId, skillLevel, 0, true));
				}
			}
		}
		catch (SQLException e)
		{
			_log.warn("failed to select pet skill " + getNpcId(), e);
		}
		petSkills.addAll(Arrays.asList(_data.getSkills()));
		for (PetSkillData skillData : petSkills)
		{
			SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillData.getId(), skillData.getLevel(getLevel()));
			if (skillEntry == null)
				continue;

			int haveSkillLevel = getSkillLevel(skillEntry.getId(), 0);
			if (skillEntry.getLevel() == haveSkillLevel)
				continue;

			removeSkillById(skillEntry.getId());
			addSkill(skillEntry);
		}
	}

	@Override
	public void onAttacked(Creature attacker)
	{
		if (isAttackingNow())
			return;

		if (attacker == null || getPlayer() == null)
			return;

		if (getMovement().isMoving() || isMovementDisabled() || getAI().getIntention() != CtrlIntention.AI_INTENTION_FOLLOW)
			return;

		Player player = getPlayer();
		if (player == null)
			return;

		getMovement().moveToLocation(Location.findPointToStay(getPlayer().getLoc(), Config.FOLLOW_RANGE, Config.FOLLOW_RANGE, getGeoIndex()), 0, true); // TODO:
																																						// Check
	}

	@Override
	public void onOwnerOfAttacks(Creature target)
	{
		if (isAttackingNow())
			return;

		if (target == null || getPlayer() == null)
			return;

		if (getMovement().isMoving() || isMovementDisabled() || getAI().getIntention() != CtrlIntention.AI_INTENTION_FOLLOW)
			return;

		Player player = getPlayer();
		if (player == null)
			return;

		getMovement().moveToLocation(Location.findPointToStay(getPlayer().getLoc(), Config.FOLLOW_RANGE, Config.FOLLOW_RANGE, getGeoIndex()), 0, true); // TODO:
																																						// Check
	}

	private EvolveLevel evolveLevel = EvolveLevel.None;

	@Override
	public int getPetType()
	{
		return _data.getPetType();
	}

	@Override
	public int getEvolveLevel()
	{
		return evolveLevel.ordinal();
	}

	@Override
	public void setEvolveLevel(EvolveLevel evolveLevel)
	{
		this.evolveLevel = evolveLevel;
	}

	private class FeedTask implements Runnable
	{
		@Override
		public void run()
		{
			if (getData().isOfType(PetType.SPECIAL))
			{
				if (getCurrentFed() <= 0)
				{
					rewardOwner();
					unSummon(false);
					destroyControlItem();
				}
			}
			else
			{
				if (isHungry())
					tryFeed();

				if (getCurrentFed() <= (int) (getMaxFed() * 0.10))
					getPlayer().sendPacket(SystemMsg.WHEN_YOUR_PETS_HUNGER_GAUGE_IS_AT_0_YOU_CANNOT_USE_YOUR_PET);
				else if (getCurrentFed() <= 0)
				{
					getPlayer().sendPacket(SystemMsg.YOUR_PET_IS_STARVING_AND_WILL_NOT_OBEY_UNTIL_IT_GETS_ITS_FOOD);
					return;
				}
			}

			consumeMeal();

			ItemInstance item = getControlItem();
			if (item != null && !item.getTemplate().testCondition(getPlayer(), item, false))
			{
				unSummon(false);
			}
		}
	}
}