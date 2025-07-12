package l2s.gameserver.model.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

import l2s.commons.collections.LazyArrayList;
import l2s.commons.dao.JdbcEntity;
import l2s.commons.dao.JdbcEntityState;
import l2s.commons.listener.Listener;
import l2s.gameserver.Config;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.dao.HidenItemsDAO;
import l2s.gameserver.dao.ItemsDAO;
import l2s.gameserver.dao.ItemsEnsoulDAO;
import l2s.gameserver.data.xml.holder.AppearanceStoneHolder;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.handler.onshiftaction.OnShiftActionHolder;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.listener.inventory.OnEquipListener;
import l2s.gameserver.model.CommandChannel;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.PlayerGroup;
import l2s.gameserver.model.base.Element;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.attachment.ItemAttachment;
import l2s.gameserver.network.l2.s2c.DropItemPacket;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.network.l2.s2c.SpawnItemPacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.stats.Env;
import l2s.gameserver.stats.StatModifierType;
import l2s.gameserver.stats.funcs.Func;
import l2s.gameserver.stats.funcs.FuncTemplate;
import l2s.gameserver.stats.triggers.TriggerType;
import l2s.gameserver.taskmanager.ItemsAutoDestroy;
import l2s.gameserver.taskmanager.LazyPrecisionTaskManager;
import l2s.gameserver.templates.OptionDataTemplate;
import l2s.gameserver.templates.item.ExItemType;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.ItemType;
import l2s.gameserver.templates.item.support.AppearanceStone;
import l2s.gameserver.templates.item.support.Ensoul;
import l2s.gameserver.templates.pet.PetParam;
import l2s.gameserver.utils.ItemFunctions;

public final class ItemInstance extends GameObject implements JdbcEntity
{
	public static final int[] EMPTY_ENCHANT_OPTIONS = new int[3];
	public static final List<Ensoul> EMPTY_ENSOULS_ARRAY = Collections.emptyList();

	private static final ItemsDAO _itemsDAO = ItemsDAO.getInstance();

	public static final int FLAG_NO_DROP = 1 << 0;
	public static final int FLAG_NO_TRADE = 1 << 1;
	public static final int FLAG_NO_TRANSFER = 1 << 2;
	public static final int FLAG_NO_CRYSTALLIZE = 1 << 3;
	public static final int FLAG_NO_ENCHANT = 1 << 4;
	public static final int FLAG_NO_DESTROY = 1 << 5;
	public static final int FLAG_NO_SHAPE_SHIFTING = 1 << 6;
	public static final int FLAG_LIFE_TIME = 1 << 6;

	/** ID of the owner */
	private int _ownerId;
	/** ID of the item */
	private int _itemId;
	/** Quantity of the item */
	private long _count;
	/** Level of enchantment of the item */
	private int _enchantLevel = -1;
	/** Location of the item */
	private ItemLocation _loc;
	/** Slot where item is stored */
	private int _locData;
	/** Custom item types (used loto, race tickets) */
	private int _customType1;
	private int _customType2;
	/** Время жизни временных вещей */
	private int _lifeTime;
	/** Спецфлаги для конкретного инстанса */
	private int _customFlags;
	/** Атрибуты вещи */
	private ItemAttributes _attrs = new ItemAttributes();
	/** Аугментация вещи */
	private int[] _enchantOptions = EMPTY_ENCHANT_OPTIONS;

	/** Object L2Item associated to the item */
	private ItemTemplate _template;
	/** Флаг, что вещь одета, выставляется в инвентаре **/
	private boolean _isEquipped;

	/** Item drop time for autodestroy task */
	private long _dropTime;

	private IntSet _dropPlayers = Containers.EMPTY_INT_SET;
	private long _dropTimeOwner;

	// Charged shot's power.
	private double _chargedSoulshotPower = 0;
	private double _chargedSpiritshotPower = 0;
	private double _chargedSpiritshotHealBonus = 0;
	private double _chargedFishshotPower = 0;

	private int _agathionEnergy;
	private int _visualId;

	private int _variationStoneId = 0;
	private int _variation1Id = 0;
	private int _variation2Id = 0;

	private ItemAttachment _attachment;
	private JdbcEntityState _state = JdbcEntityState.CREATED;

	private int _appearanceStoneId = 0;
	private List<SkillEntry> _appearanceStoneSkills = null;

	private final Ensoul[] _ensoulOptions = new Ensoul[2];
	private final Ensoul[] _ensoulSpecialOptions = new Ensoul[1];

	private final Lock _onEquipUnequipLock = new ReentrantLock();

	private Map<Object, IntObjectMap<SkillEntry>> _equippedSkills = null;
	private Map<Object, IntObjectMap<OptionDataTemplate>> _equippedOptionDatas = null;

	private boolean _blessed = false;
	private int _lostDate = 0;
	private PetItemInfo _petInfo;

	private ScheduledFuture<?> _manaConsumeTask;

	public ItemInstance(int objectId)
	{
		super(objectId);
	}

	/**
	 * Constructor<?> of the L2ItemInstance from the objectId and the itemId.
	 * 
	 * @param objectId : int designating the ID of the object in the world
	 * @param itemId   : int designating the ID of the item
	 */
	public ItemInstance(int objectId, int itemId)
	{
		super(objectId);
		setItemId(itemId);
		setLifeTime(-1);
		setAgathionEnergy(getTemplate().getAgathionEnergy());
		setLocData(-1);
		setEnchantLevel(getTemplate().getBaseEnchantLevel());
	}

	public int getOwnerId()
	{
		return _ownerId;
	}

	public void setOwnerId(int ownerId)
	{
		_ownerId = ownerId;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public void setItemId(int id)
	{
		_itemId = id;
		_template = ItemHolder.getInstance().getTemplate(id);
		setCustomFlags(getCustomFlags());
	}

	public long getCount()
	{
		return _count;
	}

	public void setCount(long count)
	{
		if (count < 0)
		{
			count = 0;
		}

		if (!isStackable() && (count > 1L))
		{
			_count = 1L;
			// TODO audit
			return;
		}

		_count = count;
	}

	public int getEnchantLevel()
	{
		return _enchantLevel;
	}

	public int getFixedEnchantLevel(Player owner)
	{
		if (owner != null)
		{
			if (_enchantLevel > 0)
			{
				if (Config.OLYMPIAD_ENABLE_ENCHANT_LIMIT && owner.isInOlympiadMode())
				{
					if (isWeapon())
					{
						return Math.min(Config.OLYMPIAD_WEAPON_ENCHANT_LIMIT, _enchantLevel);
					}
					if (isArmor())
					{
						return Math.min(Config.OLYMPIAD_ARMOR_ENCHANT_LIMIT, _enchantLevel);
					}
					if (isAccessory())
					{
						return Math.min(Config.OLYMPIAD_JEWEL_ENCHANT_LIMIT, _enchantLevel);
					}
				}
			}
		}
		return _enchantLevel;
	}

	public void setEnchantLevel(int value)
	{
		final int old = _enchantLevel;

		_enchantLevel = Math.max(getTemplate().getBaseEnchantLevel(), value);
		_enchantOptions = EMPTY_ENCHANT_OPTIONS;

		if ((old != _enchantLevel) && (getTemplate().getEnchantOptions().size() > 0))
		{
			int[] enchantOptions = null;
			for (int i = _enchantLevel; i >= 0; i--)
			{
				enchantOptions = getTemplate().getEnchantOptions().get(_enchantLevel);
				if (enchantOptions != null)
				{
					_enchantOptions = enchantOptions;
					break;
				}
			}
		}
	}

	public void setLocName(String loc)
	{
		_loc = ItemLocation.valueOf(loc);
	}

	public String getLocName()
	{
		return _loc.name();
	}

	public void setLocation(ItemLocation loc)
	{
		_loc = loc;
	}

	public ItemLocation getLocation()
	{
		return _loc;
	}

	public void setLocData(int locData)
	{
		_locData = locData;
	}

	public int getLocData()
	{
		return _locData;
	}

	public int getCustomType1()
	{
		return _customType1;
	}

	public void setCustomType1(int newtype)
	{
		_customType1 = newtype;
	}

	public int getCustomType2()
	{
		return _customType2;
	}

	public void setCustomType2(int newtype)
	{
		_customType2 = newtype;
	}

	public int getLifeTime()
	{
		return _lifeTime;
	}

	public void setLifeTime(int lifeTime)
	{
		if (lifeTime == -1)
		{
			_lifeTime = getTemplate().isTemporal() ? (int) (System.currentTimeMillis() / 1000L) + (getTemplate().getDurability() * 60) : (getTemplate().isShadowItem() ? getTemplate().getDurability() : -1);
		}
		else
		{
			_lifeTime = Math.max(0, lifeTime);
		}
	}

	public int getCustomFlags()
	{
		return _customFlags;
	}

	public void setCustomFlags(int flags)
	{
		_customFlags = flags;
	}

	public ItemAttributes getAttributes()
	{
		return _attrs;
	}

	public void setAttributes(ItemAttributes attrs)
	{
		_attrs = attrs;
	}

	public int getShadowLifeTime()
	{
		if (!isShadowItem())
		{
			return -1;
		}
		return getLifeTime();
	}

	public int getTemporalLifeTime()
	{
		if (((getVisualId() > 0) && (getLifeTime() >= 0)) || isTemporalItem() || isFlagLifeTime())
		{
			return getLifeTime() - (int) (System.currentTimeMillis() / 1000L);
		}
		return -9999;
	}

	public void startManaConsumeTask(PcInventory.ManaConsumeTask r)
	{
		if (_manaConsumeTask == null)
		{
			_manaConsumeTask = LazyPrecisionTaskManager.getInstance().scheduleAtFixedRate(r, 0, 60000L);
		}
	}

	public void stopManaConsumeTask()
	{
		if (_manaConsumeTask != null)
		{
			_manaConsumeTask.cancel(false);
			_manaConsumeTask = null;
		}
	}

	/**
	 * Returns if item is equipable
	 * 
	 * @return boolean
	 */
	public boolean isEquipable()
	{
		return _template.isEquipable();
	}

	/**
	 * Returns if item is equipped
	 * 
	 * @return boolean
	 */
	public boolean isEquipped()
	{
		return _isEquipped;
	}

	public void setEquipped(boolean isEquipped)
	{
		_isEquipped = isEquipped;
	}

	public long getBodyPart()
	{
		return _template.getBodyPart();
	}

	/**
	 * Returns the slot where the item is stored
	 * 
	 * @return int
	 */
	public int getEquipSlot()
	{
		return getLocData();
	}

	/**
	 * Returns the characteristics of the item
	 * 
	 * @return L2Item
	 */
	public ItemTemplate getTemplate()
	{
		return _template;
	}

	public void setDropTime(long time)
	{
		_dropTime = time;
	}

	public long getLastDropTime()
	{
		return _dropTime;
	}

	public long getDropTimeOwner()
	{
		return _dropTimeOwner;
	}

	/**
	 * Returns the type of item
	 * 
	 * @return Enum
	 */
	public ItemType getItemType()
	{
		return _template.getItemType();
	}

	public boolean isArmor()
	{
		return _template.isArmor();
	}

	public boolean isAccessory()
	{
		return _template.isAccessory();
	}

	public boolean isOther()
	{
		return _template.isOther();
	}

	public boolean isWeapon()
	{
		return _template.isWeapon();
	}

	/**
	 * Returns the reference price of the item
	 * 
	 * @return int
	 */
	public int getReferencePrice()
	{
		return _template.getReferencePrice();
	}

	/**
	 * Returns if item is stackable
	 * 
	 * @return boolean
	 */
	public boolean isStackable()
	{
		return _template.isStackable();
	}

	@Override
	public void onAction(Player player, boolean shift)
	{
		if (shift && OnShiftActionHolder.getInstance().callShiftAction(player, ItemInstance.class, this, true))
		{
			return;
		}

		player.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, this, null);
	}

	public boolean isAugmented()
	{
		if (!getTemplate().isHairAccessory())
		{
			return (getVariation1Id() != 0) && (getVariation2Id() != 0);
		}
		else
		{
			return (getVariation1Id() != 0) || (getVariation2Id() != 0);
		}
	}

	public int getVariation1Id()
	{
		return _variation1Id;
	}

	public void setVariation1Id(int val)
	{
		_variation1Id = val;
	}

	public int getVariation2Id()
	{
		return _variation2Id;
	}

	public void setVariation2Id(int val)
	{
		_variation2Id = val;
	}

	public void setVariation3Id(int val)
	{

	}
	
	public boolean isBlessed()
	{
		return _blessed;
	}

	public void setBlessed(boolean val)
	{
		_blessed = val;
	}

	public int getLostDate()
	{
		return _lostDate;
	}

	public void setLostDate(int date)
	{
		_lostDate = date;
	}

	public class FuncAttack extends Func
	{
		private final Element _element;

		public FuncAttack(Element element, int order, Object owner)
		{
			super(element.getAttack(), order, owner);
			_element = element;
		}

		@Override
		public void calc(Env env, StatModifierType modifierType)
		{
			env.value += getAttributeElementValue(_element, true);
		}
	}

	public class FuncDefence extends Func
	{
		private final Element _element;

		public FuncDefence(Element element, int order, Object owner)
		{
			super(element.getDefence(), order, owner);
			_element = element;
		}

		@Override
		public void calc(Env env, StatModifierType modifierType)
		{
			env.value += getAttributeElementValue(_element, true);
		}
	}

	/**
	 * This function basically returns a set of functions from
	 * L2Item/L2Armor/L2Weapon, but may add additional functions, if this particular
	 * item instance is enhanched for a particular player.
	 * 
	 * @return Func[]
	 */
	public Func[] getStatFuncs()
	{
		Func[] result = Func.EMPTY_FUNC_ARRAY;

		final LazyArrayList<Func> funcs = LazyArrayList.newInstance();

		if (_template.getAttachedFuncs().length > 0)
		{
			for (final FuncTemplate t : _template.getAttachedFuncs())
			{
				final Func f = t.getFunc(this);
				if (f != null)
				{
					funcs.add(f);
				}
			}
		}

		for (final Element e : Element.VALUES)
		{
			if (isWeapon())
			{
				funcs.add(new FuncAttack(e, 0x40, this));
			}
			if (isArmor())
			{
				funcs.add(new FuncDefence(e, 0x40, this));
			}
		}

		if (!funcs.isEmpty())
		{
			result = funcs.toArray(new Func[funcs.size()]);
		}

		LazyArrayList.recycle(funcs);

		return result;
	}

	/**
	 * Return true if item is hero-item
	 * 
	 * @return boolean
	 */
	public boolean isHeroWeapon()
	{
		return _template.isHeroWeapon();
	}

	public boolean isHeroItem()
	{
		return _template.isHeroItem();
	}

	public boolean isOlympiadItem()
	{
		return _template.isOlympiadItem();
	}

	/**
	 * Return true if item can be destroyed
	 */
	public boolean canBeDestroyed(Player player)
	{
		if (((_customFlags & FLAG_NO_DESTROY) == FLAG_NO_DESTROY) || isHeroItem() || (player.getMountControlItemObjId() == getObjectId()) || (player.getPetControlItem() == this))
		{
			return false;
		}

		if (player.getEnchantScroll() == this)
		{
			return false;
		}

		return _template.isDestroyable();
	}

	/**
	 * Return true if item can be dropped
	 */
	public boolean canBeDropped(Player player, boolean pk)
	{
		if (player.isGM())
		{
			return true;
		}

		if (HidenItemsDAO.isHidden(this) || ((_customFlags & FLAG_NO_DROP) == FLAG_NO_DROP) || isShadowItem() || isTemporalItem())
		{
			return false;
		}

		if (isAugmented() && (!pk || !Config.DROP_ITEMS_AUGMENTED) && !Config.ALT_ALLOW_DROP_AUGMENTED)
		{
			return false;
		}

		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}

		return _template.isDropable();
	}

	public boolean canBeTraded(Player player)
	{
		if (isEquipped())
		{
			return false;
		}

		if (player.isGM() || Config.LIST_OF_TRABLE_ITEMS.equals(getItemId()))
		{
			return true;
		}

		if (HidenItemsDAO.isHidden(this) || ((_customFlags & FLAG_NO_TRADE) == FLAG_NO_TRADE) || isShadowItem() || isTemporalItem())
		{
			return false;
		}

		if ((isAugmented() && !Config.ALT_ALLOW_DROP_AUGMENTED) || !ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}

		return _template.isTradeable();
	}

	public boolean canBePrivateStore(Player player)
	{
		if ((getItemId() == ItemTemplate.ITEM_ID_ADENA) || !canBeTraded(player))
		{
			return false;
		}

		return _template.isPrivatestoreable();
	}

	/**
	 * Можно ли продать в магазин NPC
	 */
	public boolean canBeSold(Player player)
	{
		if (((_customFlags & FLAG_NO_DESTROY) == FLAG_NO_DESTROY) || ((_customFlags & FLAG_NO_TRADE) == FLAG_NO_TRADE) || (getItemId() == ItemTemplate.ITEM_ID_ADENA) || HidenItemsDAO.isHidden(this))
		{
			return false;
		}

		if (Config.LIST_OF_SELLABLE_ITEMS.equals(getItemId()))
		{
			return true;
		}

		if (isShadowItem() || isTemporalItem() || (isAugmented() && !Config.ALT_ALLOW_DROP_AUGMENTED))
		{
			return false;
		}

		if (isEquipped())
		{
			return false;
		}

		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}

		if (!_template.isDestroyable())
		{
			return false;
		}

		return _template.isSellable();
	}

	/**
	 * Можно ли положить на клановый склад
	 */
	public boolean canBeStored(Player player, boolean privatewh)
	{
		if (((_customFlags & FLAG_NO_TRANSFER) == FLAG_NO_TRANSFER) || !getTemplate().isStoreable())
		{
			return false;
		}

		if (!privatewh && (isShadowItem() || isTemporalItem()))
		{
			return false;
		}

		if ((!privatewh && isAugmented() && !Config.ALT_ALLOW_DROP_AUGMENTED) || isEquipped())
		{
			return false;
		}

		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}

		if (HidenItemsDAO.isHidden(this))
		{
			return false;
		}

		return privatewh || _template.isTradeable();
	}

	public boolean canBeCrystallized(Player player)
	{
		if (isFlagNoCrystallize() || isHeroItem() || isShadowItem() || isTemporalItem())
		{
			return false;
		}

		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}

		return _template.isCrystallizable();
	}

	public boolean canBeEnchanted()
	{
		if (((_customFlags & FLAG_NO_ENCHANT) == FLAG_NO_ENCHANT) || isHeroItem() || isShadowItem() || isTemporalItem())
		{
			return false;
		}

		if (isCommonItem())
		{
			return false;
		}

		return _template.canBeEnchanted();
	}

	public boolean canBeAppearance()
	{
		if (!isEquipable() || isHeroItem() || isShadowItem() || isTemporalItem())
		{
			return false;
		}

		if (isCommonItem())
		{
			return false;
		}

		return _template.canBeAppearance();
	}

	public boolean canBeAugmented(Player player)
	{
		if (!getTemplate().isAugmentable() || isHeroItem() || isShadowItem() || isTemporalItem())
		{
			return false;
		}

		if (isCommonItem() || _template.isPvP())
		{
			return false;
		}

		return true;
	}

	public boolean canBeBlessed()
	{
		if (!getTemplate().isBlessable() || isBlessed() || isHeroItem() || isShadowItem())
		{
			return false;
		}

		if (isTemporalItem() || isCommonItem())
		{
			return false;
		}

		return true;
	}

	public boolean canBeExchanged(Player player)
	{
		if (((_customFlags & FLAG_NO_DESTROY) == FLAG_NO_DESTROY) || isShadowItem() || isTemporalItem() || !ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}

		if (HidenItemsDAO.isHidden(this))
		{
			return false;
		}

		return _template.isDestroyable();
	}

	public boolean canBeEnsoul(int ensoulId)
	{
		if (isHeroItem() || isShadowItem() || isTemporalItem() || isCommonItem())
		{
			return false;
		}

		return _template.canBeEnsoul(ensoulId);
	}

	public boolean isShadowItem()
	{
		return _template.isShadowItem();
	}

	public boolean isTemporalItem()
	{
		return _template.isTemporal();
	}

	public boolean isCommonItem()
	{
		return _template.isCommonItem();
	}

	public boolean isHiddenItem()
	{
		return HidenItemsDAO.isHidden(this);
	}

	/**
	 * Бросает на землю лут с NPC
	 */
	public void dropToTheGround(Player lastAttacker, NpcInstance fromNpc)
	{
		Creature dropper = fromNpc;
		if (dropper == null)
		{
			dropper = lastAttacker;
		}

		final Location pos = Location.findAroundPosition(dropper, 100);

		// activate non owner penalty
		if (lastAttacker != null) // lastAttacker в данном случае top damager
		{
			_dropPlayers = new HashIntSet(1, 2);

			PlayerGroup group = lastAttacker.getParty();
			if (group == null)
			{
				group = lastAttacker;
			}

			if ((fromNpc != null) && fromNpc.isBoss()) // На эпиках, дроп поднимает лидер CC.
			{
				group = lastAttacker.getPlayerGroup();
				if ((group != null) && (group instanceof CommandChannel))
				{
					final Player ccLeader = group.getGroupLeader();
					if (ccLeader != null)
					{
						group = ccLeader.getParty();
						if (group == null)
						{
							group = lastAttacker;
						}
					}
				}
			}

			for (final Player $member : group)
			{
				_dropPlayers.add($member.getObjectId());
			}

			_dropTimeOwner = System.currentTimeMillis() + Config.NONOWNER_ITEM_PICKUP_DELAY + ((fromNpc != null) && fromNpc.isRaid() ? 285000 : 0);
		}

		// Init the dropped L2ItemInstance and add it in the world as a visible object
		// at the position where mob was last
		dropMe(dropper, pos);
	}

	/**
	 * Бросает вещь на землю туда, где ее можно поднять
	 */
	public void dropToTheGround(Creature dropper, Location dropPos)
	{
		if (GeoEngine.canMoveToCoord(dropper.getX(), dropper.getY(), dropper.getZ(), dropPos.x, dropPos.y, dropPos.z, dropper.getGeoIndex()))
		{
			dropMe(dropper, dropPos);
		}
		else
		{
			dropMe(dropper, dropper.getLoc());
		}
	}

	/**
	 * Бросает вещь на землю из инвентаря туда, где ее можно поднять
	 */
	public void dropToTheGround(Playable dropper, Location dropPos)
	{
		setLocation(ItemLocation.VOID);
		if (getJdbcState().isPersisted())
		{
			setJdbcState(JdbcEntityState.UPDATED);
			update();
		}

		if (GeoEngine.canMoveToCoord(dropper.getX(), dropper.getY(), dropper.getZ(), dropPos.x, dropPos.y, dropPos.z, dropper.getGeoIndex()))
		{
			dropMe(dropper, dropPos);
		}
		else
		{
			dropMe(dropper, dropper.getLoc());
		}
	}

	/**
	 * Init a dropped L2ItemInstance and add it in the world as a visible
	 * object.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Set the x,y,z position of the L2ItemInstance dropped and update its
	 * _worldregion</li>
	 * <li>Add the L2ItemInstance dropped to _visibleObjects of its
	 * L2WorldRegion</li>
	 * <li>Add the L2ItemInstance dropped in the world as a <B>visible</B>
	 * object</li><BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T ADD the object
	 * to _allObjects of L2World </B></FONT><BR>
	 * <BR>
	 * <B><U> Assert </U> :</B><BR>
	 * <BR>
	 * <li>this instanceof L2ItemInstance</li>
	 * <li>_worldRegion == null <I>(L2Object is invisible at the
	 * beginning)</I></li><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Drop item</li>
	 * <li>Call Pet</li><BR>
	 *
	 * @param dropper Char that dropped item
	 * @param loc     drop coordinates
	 */
	public void dropMe(Creature dropper, Location loc)
	{
		if (dropper != null)
		{
			setReflection(dropper.getReflection());
		}

		spawnMe0(loc, dropper);

		if ((dropper != null) && dropper.isPlayable())
		{
			if (Config.AUTODESTROY_PLAYER_ITEM_AFTER > 0)
			{
				ItemsAutoDestroy.getInstance().addPlayerItem(this);
			}
		}
		else
		{
			// Add drop to auto destroy item task
			if (isHerb())
			{
				ItemsAutoDestroy.getInstance().addHerb(this);
			}
			else if (Config.AUTODESTROY_ITEM_AFTER > 0)
			{
				ItemsAutoDestroy.getInstance().addItem(this);
			}
		}
	}

	public final void pickupMe()
	{
		decayMe();
		setReflection(ReflectionManager.MAIN);
	}

	/**
	 * Возвращает защиту от элемента.
	 * 
	 * @return значение защиты
	 */
	private int getDefence(Element element)
	{
		return isArmor() ? getAttributeElementValue(element, true) : 0;
	}

	/**
	 * Возвращает защиту от элемента: огонь.
	 * 
	 * @return значение защиты
	 */
	public int getDefenceFire()
	{
		return getDefence(Element.FIRE);
	}

	/**
	 * Возвращает защиту от элемента: вода.
	 * 
	 * @return значение защиты
	 */
	public int getDefenceWater()
	{
		return getDefence(Element.WATER);
	}

	/**
	 * Возвращает защиту от элемента: воздух.
	 * 
	 * @return значение защиты
	 */
	public int getDefenceWind()
	{
		return getDefence(Element.WIND);
	}

	/**
	 * Возвращает защиту от элемента: земля.
	 * 
	 * @return значение защиты
	 */
	public int getDefenceEarth()
	{
		return getDefence(Element.EARTH);
	}

	/**
	 * Возвращает защиту от элемента: свет.
	 * 
	 * @return значение защиты
	 */
	public int getDefenceHoly()
	{
		return getDefence(Element.HOLY);
	}

	/**
	 * Возвращает защиту от элемента: тьма.
	 * 
	 * @return значение защиты
	 */
	public int getDefenceUnholy()
	{
		return getDefence(Element.UNHOLY);
	}

	/**
	 * Возвращает значение элемента.
	 * 
	 * @return
	 */
	public int getAttributeElementValue(Element element, boolean withBase)
	{
		return _attrs.getValue(element) + (withBase ? _template.getBaseAttributeValue(element) : 0);
	}

	/**
	 * Возвращает элемент атрибуции предмета.<br>
	 */
	public Element getAttributeElement()
	{
		return _attrs.getElement();
	}

	public int getAttributeElementValue()
	{
		return _attrs.getValue();
	}

	public Element getAttackElement()
	{
		final Element element = isWeapon() ? getAttributeElement() : Element.NONE;
		if (element == Element.NONE)
		{
			for (final Element e : Element.VALUES)
			{
				if (_template.getBaseAttributeValue(e) > 0)
				{
					return e;
				}
			}
		}
		return element;
	}

	public int getAttackElementValue()
	{
		return isWeapon() ? getAttributeElementValue(getAttackElement(), true) : 0;
	}

	/**
	 * Устанавливает элемент атрибуции предмета.<br>
	 * Element (0 - Fire, 1 - Water, 2 - Wind, 3 - Earth, 4 - Holy, 5 - Dark, -1 -
	 * None)
	 * 
	 * @param element элемент
	 * @param value
	 */
	public void setAttributeElement(Element element, int value)
	{
		_attrs.setValue(element, value);
	}

	/**
	 * Проверяет, является ли данный инстанс предмета хербом
	 * 
	 * @return true если предмет является хербом
	 */
	public boolean isHerb()
	{
		return getTemplate().isHerb();
	}

	public long getPriceLimitForItem()
	{
		return getTemplate().getPriceLimitForItem();
	}

	public ItemGrade getGrade()
	{
		return _template.getGrade();
	}

	@Override
	public String getName()
	{
		return getTemplate().getName();
	}

	public String getName(Player player)
	{
		return getTemplate().getName(player);
	}

	@Override
	public void save()
	{
		_onEquipUnequipLock.lock();
		try
		{
			_itemsDAO.save(this);
		}
		finally
		{
			_onEquipUnequipLock.unlock();
		}
	}

	@Override
	public void update()
	{
		_onEquipUnequipLock.lock();
		try
		{
			_itemsDAO.update(this);
		}
		finally
		{
			_onEquipUnequipLock.unlock();
		}
	}

	@Override
	public void delete()
	{
		_onEquipUnequipLock.lock();
		try
		{
			_itemsDAO.delete(this);
			ItemsEnsoulDAO.getInstance().delete(getObjectId());
			stopManaConsumeTask();
		}
		finally
		{
			_onEquipUnequipLock.unlock();
		}
	}

	@Override
	public List<IClientOutgoingPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		// FIXME кажись дроппер у нас есть в итеме как переменная, ток проверить время?
		// [VISTALL]
		IClientOutgoingPacket packet = null;
		if (dropper != null)
		{
			packet = new DropItemPacket(this, dropper.getObjectId());
		}
		else
		{
			packet = new SpawnItemPacket(this);
		}

		return Collections.singletonList(packet);
	}

	/**
	 * Returns the item in String format
	 */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();

		sb.append(getTemplate().getItemId());
		sb.append(" ");
		if (getEnchantLevel() > 0)
		{
			sb.append("+");
			sb.append(getEnchantLevel());
			sb.append(" ");
		}
		sb.append(getTemplate().getName());
		if (!getTemplate().getAdditionalName().isEmpty())
		{
			sb.append(" ");
			sb.append("\\").append(getTemplate().getAdditionalName()).append("\\");
		}
		sb.append(" ");
		sb.append("(");
		sb.append(getCount());
		sb.append(")");
		sb.append("[");
		sb.append(getObjectId());
		sb.append("]");

		return sb.toString();

	}

	@Override
	public void setJdbcState(JdbcEntityState state)
	{
		_state = state;
	}

	@Override
	public JdbcEntityState getJdbcState()
	{
		return _state;
	}

	@Override
	public boolean isItem()
	{
		return true;
	}

	public ItemAttachment getAttachment()
	{
		return _attachment;
	}

	public void setAttachment(ItemAttachment attachment)
	{
		final ItemAttachment old = _attachment;
		_attachment = attachment;
		if (_attachment != null)
		{
			_attachment.setItem(this);
		}
		if (old != null)
		{
			old.setItem(null);
		}
	}

	public int getAgathionEnergy()
	{
		return _agathionEnergy;
	}

	public void setAgathionEnergy(int agathionEnergy)
	{
		_agathionEnergy = agathionEnergy;
	}

	public int getVisualId()
	{
		return _visualId;
	}

	public void setVisualId(int val)
	{
		_visualId = val;
	}

	public int getAppearanceStoneId()
	{
		return _appearanceStoneId;
	}

	public void setAppearanceStoneId(int val)
	{
		if (val == _appearanceStoneId)
		{
			return;
		}

		_appearanceStoneId = val;

		if (_appearanceStoneSkills != null)
		{
			_appearanceStoneSkills.clear();
		}

		if (_appearanceStoneId > 0)
		{
			final AppearanceStone stone = AppearanceStoneHolder.getInstance().getAppearanceStone(_appearanceStoneId);
			if (stone != null)
			{
				if (_appearanceStoneSkills == null)
				{
					_appearanceStoneSkills = new ArrayList<SkillEntry>();
				}
				_appearanceStoneSkills.addAll(stone.getSkills());
			}
		}
	}

	public List<SkillEntry> getAppearanceStoneSkills()
	{
		if (_appearanceStoneSkills == null)
		{
			return Collections.emptyList();
		}
		return _appearanceStoneSkills;
	}

	public int[] getEnchantOptions()
	{
		return _enchantOptions;
	}

	public IntSet getDropPlayers()
	{
		return _dropPlayers;
	}

	public int getCrystalCountOnCrystallize()
	{
		final int crystalsAdd = ItemFunctions.getCrystallizeCrystalAdd(this);
		return _template.getCrystalCount() + crystalsAdd;
	}

	public int getCrystalCountOnEchant()
	{
		final int defaultCrystalCount = _template.getCrystalCount();
		if (defaultCrystalCount > 0)
		{
			final int crystalsAdd = ItemFunctions.getCrystallizeCrystalAdd(this);
			return (int) Math.ceil(defaultCrystalCount / 2.) + crystalsAdd;
		}
		return 0;
	}

	private static final int[][] ENCHANT_FAIL_WEAPON_STONES = new int[][]
	{
		{
			0
		}, // NONE
		{
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			1,
			2,
			3,
			4,
			5,
			6,
			7,
			8,
			9,
			10,
			11,
			12,
			14
		}, // D
		{
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			2,
			3,
			4,
			7,
			8,
			9,
			12,
			13,
			14,
			17,
			18,
			19,
			25
		}, // C
		{
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			3,
			4,
			5,
			8,
			9,
			10,
			13,
			14,
			15,
			18,
			19,
			20,
			28
		}, // B
		{
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			5,
			6,
			7,
			12,
			13,
			14,
			19,
			20,
			21,
			26,
			27,
			28,
			38
		}, // A
		{
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			15,
			18,
			21,
			25,
			28,
			29,
			30,
			31,
			32,
			33,
			34,
			35,
			36
		} // S
	};

	private static final int[][] ENCHANT_FAIL_ARMOR_STONES = new int[][]
	{
		{
			0
		}, // NONE
		{
			0,
			0,
			0,
			0,
			0,
			0,
			1,
			2,
			3,
			5,
			6,
			7,
			12
		}, // D
		{
			0,
			0,
			0,
			0,
			0,
			0,
			2,
			3,
			4,
			7,
			8,
			9,
			15
		}, // C
		{
			0,
			0,
			0,
			0,
			0,
			0,
			3,
			4,
			5,
			9,
			10,
			11,
			19
		}, // B
		{
			0,
			0,
			0,
			0,
			0,
			0,
			5,
			6,
			7,
			13,
			14,
			16,
			26
		}, // A
		{
			0,
			0,
			0,
			0,
			0,
			0,
			10,
			15,
			20,
			25,
			27,
			28,
			30
		} // S
	};

	public int[] getEnchantFailStone()
	{
		final int enchantLevel = getEnchantLevel();
		final int gradeOrdinal = getGrade().extOrdinal();
		int stoneId;
		int stoneCount = 0;

		if (isWeapon())
		{
			stoneId = 91462;
			if ((gradeOrdinal >= 0) && (gradeOrdinal < ENCHANT_FAIL_WEAPON_STONES.length))
			{
				final int[] countArr = ENCHANT_FAIL_WEAPON_STONES[gradeOrdinal];
				stoneCount = countArr[Math.min(enchantLevel, countArr.length - 1)];
			}
		}
		else
		{
			stoneId = 91463;
			if ((gradeOrdinal >= 0) && (gradeOrdinal < ENCHANT_FAIL_ARMOR_STONES.length))
			{
				final int[] countArr = ENCHANT_FAIL_ARMOR_STONES[gradeOrdinal];
				stoneCount = countArr[Math.min(enchantLevel, countArr.length - 1)];
			}
		}
		return new int[]
		{
			stoneId,
			stoneCount
		};
	}

	public ExItemType getExType()
	{
		return getTemplate().getExType();
	}

	public void setVariationStoneId(int id)
	{
		_variationStoneId = id;
	}

	public int getVariationStoneId()
	{
		return _variationStoneId;
	}

	public double getChargedSoulshotPower()
	{
		return _chargedSoulshotPower;
	}

	public void setChargedSoulshotPower(double val)
	{
		_chargedSoulshotPower = val;
	}

	public double getChargedSpiritshotPower()
	{
		return _chargedSpiritshotPower;
	}

	public double getChargedSpiritshotHealBonus()
	{
		return _chargedSpiritshotHealBonus;
	}

	public void setChargedSpiritshotPower(double power, int unk, double healBonus)
	{
		_chargedSpiritshotPower = power;
		_chargedSpiritshotHealBonus = healBonus;
	}

	public double getChargedFishshotPower()
	{
		return _chargedFishshotPower;
	}

	public void setChargedFishshotPower(double val)
	{
		_chargedFishshotPower = val;
	}

	public Collection<Ensoul> getSpecialAbilities()
	{
		final List<Ensoul> result = new ArrayList<>();
		for (Ensoul ensoulOption : _ensoulOptions)
		{
			if (ensoulOption != null)
			{
				result.add(ensoulOption);
			}
		}
		return result;
	}
	
	public Ensoul getEnsoul(int type, int index)
	{
		if(type==1)
			return getSpecialAbility(index);
		else if (type== 2)
			return  getAdditionalSpecialAbility(index);
		
		return null;
	}
	
	public Ensoul getSpecialAbility(int index)
	{
		return _ensoulOptions[index];
	}
	
	public Collection<Ensoul> getAdditionalSpecialAbilities()
	{
		final List<Ensoul> result = new ArrayList<>();
		for (Ensoul ensoulSpecialOption : _ensoulSpecialOptions)
		{
			if (ensoulSpecialOption != null)
			{
				result.add(ensoulSpecialOption);
			}
		}
		return result;
	}
	
	public Ensoul getAdditionalSpecialAbility(int index)
	{
		return _ensoulSpecialOptions[index];
	}
	
	public void addSpecialAbility(Ensoul option, int position, int type, boolean updateInDB)
	{
		if ((type == 1) && ((position < 0) || (position > 1))) // two first slots
			return;

		if ((type == 2) && (position != 0)) // third slot
			return;

		if (type == 1) // Adding regular ability
		{
			final Ensoul oldOption = _ensoulOptions[position];
			if (oldOption != null)
				ItemsEnsoulDAO.getInstance().delete(getOwnerId(), type, position);
				//removeSpecialAbility(oldOption);
			_ensoulOptions[position] = option;
		}
		else if (type == 2) // Adding special ability
		{
			final Ensoul oldOption = _ensoulSpecialOptions[position];
			if (oldOption != null)
				ItemsEnsoulDAO.getInstance().delete(getOwnerId(), type, position);
				//removeSpecialAbility(oldOption);
			_ensoulSpecialOptions[position] = option;
		}
		
		if (updateInDB)
			updateSpecialAbilities();
	}
	
	public void removeSpecialAbility(int position, int type)
	{
		if (type == 1)
		{
			final Ensoul option = _ensoulOptions[position];
			if (option != null)
			{
				ItemsEnsoulDAO.getInstance().delete(getObjectId(), type, position);
				_ensoulOptions[position] = null;
				if (position == 0)
				{
					final Ensoul secondEnsoul = _ensoulOptions[1];
					if (secondEnsoul != null)
					{
						ItemsEnsoulDAO.getInstance().delete(getObjectId(), type, 1);
						_ensoulOptions[1] = null;
						addSpecialAbility(secondEnsoul, 0, type, true);
					}
				}
			}
		}
		else if (type == 2)
		{
			final Ensoul option = _ensoulSpecialOptions[position];
			if (option != null)
			{
				ItemsEnsoulDAO.getInstance().delete(getObjectId(), type, position);
				_ensoulSpecialOptions[position] = null;
			}
		}
	}
	
	public void updateSpecialAbilities()
	{
		ItemsEnsoulDAO.getInstance().insert(getObjectId(), _ensoulOptions, _ensoulSpecialOptions);
	}

	public int[] getEnsoulOptionsArray()
	{
		int[] ids = new int[_ensoulOptions.length]; 

		for (int i = 0; i < _ensoulOptions.length; i++) 
		{
			if (_ensoulOptions[i] != null)
				ids[i] = _ensoulOptions[i].getId(); 
		}
		return ids;
	}

	public int[] getEnsoulSpecialOptionsArray()
	{
		int[] ids = new int[_ensoulSpecialOptions.length]; 

		for (int i = 0; i < _ensoulSpecialOptions.length; i++) 
		{
			if (_ensoulSpecialOptions[i] != null)
				ids[i] = _ensoulSpecialOptions[i].getId();
		}
		return ids;
	}
	
	public  List<Ensoul> getNormalEnsouls()
	{
		final List<Ensoul> result = new ArrayList<>();
		for (Ensoul ensoulOption : _ensoulOptions)
		{
			if (ensoulOption != null)
				result.add(ensoulOption);
		}
		return result;
	}
	
	public  List<Ensoul> getSpecialEnsouls()
	{
		final List<Ensoul> result = new ArrayList<>();
		for (Ensoul ensoulSpecialOption : _ensoulSpecialOptions)
		{
			if (ensoulSpecialOption != null)
				result.add(ensoulSpecialOption);
		}
		return result;
	}

	public boolean containsEnsoul(int type, int id)
	{
		return getEnsoul(type,id-1)!=null;
	}
	
	public boolean isFlagLifeTime()
	{
		return (_customFlags & FLAG_LIFE_TIME) == FLAG_LIFE_TIME;
	}

	public boolean isFlagNoCrystallize()
	{
		return (_customFlags & FLAG_NO_CRYSTALLIZE) == FLAG_NO_CRYSTALLIZE;
	}

	public void onEquip(int slot, Playable actor)
	{
		if (!isEquipped() && !getTemplate().isRune())
		{
			return;
		}

		_onEquipUnequipLock.lock();
		try
		{
			int flags = 0;
			for (final Listener<Playable> listener : actor.getInventory().getListeners())
			{
				flags |= ((OnEquipListener) listener).onEquip(slot, this, actor);
			}

			if ((flags & Inventory.UPDATE_STATS_FLAG) == Inventory.UPDATE_STATS_FLAG)
			{
				actor.updateStats();
			}

			if ((flags & Inventory.UPDATE_SKILLS_FLAG) == Inventory.UPDATE_SKILLS_FLAG)
			{
				if (actor.isPlayer())
				{
					actor.getPlayer().sendSkillList();
				}
			}

			actor.useTriggers(actor, TriggerType.ON_EQUIP, null, null, getTemplate(), 0.);
		}
		finally
		{
			_onEquipUnequipLock.unlock();
		}
	}

	public void onEquip(Playable actor)
	{
		onEquip(getEquipSlot(), actor);
	}

	public void onUnequip(int slot, Playable actor, boolean refreshEquip)
	{
		// Слушатели снятия можно применить и на одетой вещи.
		if (!isEquipable() && !getTemplate().isRune())
		{
			return;
		}

		_onEquipUnequipLock.lock();
		try
		{
			int flags = 0;
			for (final Listener<Playable> listener : actor.getInventory().getListeners())
			{
				flags |= ((OnEquipListener) listener).onUnequip(slot, this, actor);
			}

			if (refreshEquip)
			{
				for (final ItemInstance item : actor.getInventory().getItems())
				{
					if (item != this)
					{
						flags |= item.onRefreshEquip(actor, false);
					}
				}
			}

			if ((flags & Inventory.UPDATE_STATS_FLAG) == Inventory.UPDATE_STATS_FLAG)
			{
				actor.updateStats();
			}

			if ((flags & Inventory.UPDATE_SKILLS_FLAG) == Inventory.UPDATE_SKILLS_FLAG)
			{
				if (actor.isPlayer())
				{
					actor.getPlayer().sendSkillList();
				}
			}

			actor.useTriggers(actor, TriggerType.ON_UNEQUIP, null, null, getTemplate(), 0.);
		}
		finally
		{
			_onEquipUnequipLock.unlock();
		}
	}

	public void onUnequip(int slot, Playable actor)
	{
		onUnequip(slot, actor, true);
	}

	public void onUnequip(Playable actor)
	{
		onUnequip(getEquipSlot(), actor);
	}

	public int onRefreshEquip(Playable actor, boolean update)
	{
		if (!isEquipped() && !getTemplate().isRune())
		{
			return 0;
		}

		_onEquipUnequipLock.lock();
		try
		{
			int flags = 0;
			for (final Listener<Playable> listener : actor.getInventory().getListeners())
			{
				flags |= ((OnEquipListener) listener).onRefreshEquip(this, actor);
			}

			if (update)
			{
				if ((flags & Inventory.UPDATE_STATS_FLAG) == Inventory.UPDATE_STATS_FLAG)
				{
					actor.updateStats();
				}

				if ((flags & Inventory.UPDATE_SKILLS_FLAG) == Inventory.UPDATE_SKILLS_FLAG)
				{
					if (actor.isPlayer())
					{
						actor.getPlayer().sendSkillList();
					}
				}
			}
			actor.useTriggers(actor, TriggerType.ON_EQUIP, null, null, getTemplate(), 0.);
			return flags;
		}
		finally
		{
			_onEquipUnequipLock.unlock();
		}
	}

	public int onRefreshEquip(Playable actor)
	{
		return onRefreshEquip(actor, true);
	}

	public SkillEntry addEquippedSkill(Object owner, SkillEntry skill)
	{
		if (_equippedSkills == null)
		{
			_equippedSkills = new ConcurrentHashMap<>();
		}

		final IntObjectMap<SkillEntry> skillsMap = _equippedSkills.computeIfAbsent(owner, (m) -> new HashIntObjectMap<>());
		return skillsMap.put(skill.getId(), skill);
	}

	public IntObjectMap<SkillEntry> removeEquippedSkills(Object owner)
	{
		if (_equippedSkills == null)
		{
			return null;
		}
		return _equippedSkills.remove(owner);
	}

	public int getEquippedSkillLevel(int skillId)
	{
		if (_equippedSkills == null)
		{
			return 0;
		}

		int skillLevel = 0;
		for (final IntObjectMap<SkillEntry> skillsMap : _equippedSkills.values())
		{
			final SkillEntry skillEntry = skillsMap.get(skillId);
			if (skillEntry != null)
			{
				if (skillEntry.getLevel() > skillLevel)
				{
					skillLevel = skillEntry.getLevel();
				}
			}
		}
		return skillLevel;
	}

	public OptionDataTemplate addEquippedOptionData(Object owner, OptionDataTemplate optionData)
	{
		if (_equippedOptionDatas == null)
		{
			_equippedOptionDatas = new ConcurrentHashMap<>();
		}

		final IntObjectMap<OptionDataTemplate> optionDataMap = _equippedOptionDatas.computeIfAbsent(owner, (m) -> new HashIntObjectMap<>());
		return optionDataMap.put(optionData.getId(), optionData);
	}

	public IntObjectMap<OptionDataTemplate> removeEquippedOptionDatas(Object owner)
	{
		if (_equippedOptionDatas == null)
		{
			return null;
		}
		return _equippedOptionDatas.remove(owner);
	}

	public PetItemInfo getPetInfo()
	{
		return _petInfo;
	}

	public void setPetInfo(PetItemInfo petInfo)
	{
		_petInfo = petInfo;
	}

	public int isLocked()
	{
		return 0;  
	}

	public int getVariation3Id()
	{
		return 0;
	}

	public int getUseCount()
	{
		return 0;
	}

	private PetParam _petParam = new PetParam();

	public void setPet(PetParam petParam)
	{
		_petParam = petParam;
	}

	public PetParam getPetParam()
	{
		return _petParam;
	}
	
	public boolean isDamaged()
	{
		return false;
	}

	public void setDamaged(boolean b)
	{
  
	}
}