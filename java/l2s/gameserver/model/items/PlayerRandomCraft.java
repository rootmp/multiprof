package l2s.gameserver.model.items;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.RandomCraftListHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExItemAnnounce;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftInfo;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftRandomInfo;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftRandomMake;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftRandomRefresh;
import l2s.gameserver.templates.randomCraft.RandomCraftRewardItem;
import l2s.gameserver.utils.Log;

/**
 * @author Mode
 */
public class PlayerRandomCraft
{
	private static final Logger LOGGER = Logger.getLogger(PlayerRandomCraft.class.getName());

	public static final int MAX_CRAFT_POINTS = 99;
	public static final int MAX_EXP_POINTS = 10000000;

	private final Player _player;
	private final List<RandomCraftRewardItem> _rewardList = new ArrayList<>(5);

	private int _craftPoints = 0;
	private int _exp = 0;
	private boolean _isSayhaRoll = false;

	public PlayerRandomCraft(Player player)
	{
		_player = player;
	}

	public void restore()
	{

		try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM character_random_craft WHERE charId=?"))
		{
			ps.setInt(1, _player.getObjectId());
			try (ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					try
					{
						_craftPoints = rs.getInt("random_craft_full_points");
						_exp = rs.getInt("random_craft_points");
						_isSayhaRoll = rs.getBoolean("sayha_roll");
						for(int i = 1; i <= 5; i++)
						{
							final int itemId = rs.getInt("item_" + i + "_id");
							final long itemCount = rs.getLong("item_" + i + "_count");
							final int enchant = rs.getInt("item_" + i + "_enchant");
							final boolean itemLocked = rs.getBoolean("item_" + i + "_locked");
							final int itemLockLeft = rs.getInt("item_" + i + "_lock_left");
							final RandomCraftRewardItem holder = new RandomCraftRewardItem(itemId, itemCount, enchant, itemLocked, itemLockLeft);
							_rewardList.add(i - 1, holder);
						}
					}
					catch(Exception e)
					{
						LOGGER.warning("Could not restore random craft for " + _player);
					}
				}
				else
				{
					storeNew();
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.log(Level.WARNING, "Could not restore random craft for " + _player, e);
		}
	}

	public void store()
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("UPDATE character_random_craft SET random_craft_full_points=?, random_craft_points=?, sayha_roll=?, item_1_id=?, item_1_count=?, item_1_enchant=?, item_1_locked=?, item_1_lock_left=?, item_2_id=?, item_2_count=?, item_2_enchant=?, item_2_locked=?, item_2_lock_left=?, item_3_id=?, item_3_count=?, item_3_enchant=?, item_3_locked=?, item_3_lock_left=?, item_4_id=?, item_4_count=?, item_4_enchant=?, item_4_locked=?, item_4_lock_left=?, item_5_id=?, item_5_count=?, item_5_enchant=?, item_5_locked=?, item_5_lock_left=? WHERE charId=?"))
		{
			ps.setInt(1, _craftPoints);
			ps.setInt(2, _exp);
			ps.setBoolean(3, _isSayhaRoll);
			for(int i = 0; i < 5; i++)
			{
				if(_rewardList.size() >= (i + 1))
				{
					final RandomCraftRewardItem holder = _rewardList.get(i);
					ps.setInt(4 + (i * 5), holder == null ? 0 : holder.getItemId());
					ps.setLong(5 + (i * 5), holder == null ? 0 : holder.getItemCount());
					ps.setInt(6 + (i * 5), holder == null ? 0 : holder.getEnchant());
					ps.setBoolean(7 + (i * 5), holder == null ? false : holder.isLocked());
					ps.setInt(8 + (i * 5), holder == null ? 20 : holder.getLockLeft());
				}
				else
				{
					ps.setInt(4 + (i * 5), 0);
					ps.setLong(5 + (i * 5), 0);
					ps.setInt(6 + (i * 5), 0);
					ps.setBoolean(7 + (i * 5), false);
					ps.setInt(8 + (i * 5), 20);
				}
			}
			ps.setInt(29, _player.getObjectId());
			ps.execute();
		}
		catch(Exception e)
		{
			LOGGER.log(Level.SEVERE, "Could not store RandomCraft for: " + _player, e);
		}
	}

	public void storeNew()
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("INSERT INTO character_random_craft VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"))
		{
			ps.setInt(1, _player.getObjectId());
			ps.setInt(2, _craftPoints);
			ps.setInt(3, _exp);
			ps.setBoolean(4, _isSayhaRoll);
			for(int i = 0; i < 5; i++)
			{
				ps.setInt(5 + (i * 5), 0);
				ps.setLong(6 + (i * 5), 0);
				ps.setInt(7 + (i * 5), 0);
				ps.setBoolean(8 + (i * 5), false);
				ps.setInt(9 + (i * 5), 0);
			}
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			LOGGER.log(Level.SEVERE, "Could not store new RandomCraft for: " + _player, e.getMessage());
		}
	}

	public void refresh()
	{
		if((_craftPoints > 0) && _player.reduceAdena(50000, true))
		{
			_player.sendPacket(new ExCraftInfo(_player));
			_player.sendPacket(new ExCraftRandomRefresh());
			_craftPoints--;
			if(_isSayhaRoll)
			{
				//ItemFunctions.addItem(_player,91641, 2, "PlayerRandomCraft Refresh");
				_isSayhaRoll = false;
			}
			_player.sendPacket(new ExCraftInfo(_player));

			for(int i = 0; i < 5; i++)
			{
				final RandomCraftRewardItem holder;
				if(i > (_rewardList.size() - 1))
					holder = null;
				else
					holder = _rewardList.get(i);

				if(holder == null)
					_rewardList.add(i, RandomCraftListHolder.getInstance().getNewReward(i, _rewardList));
				else if(!holder.isLocked())
					_rewardList.set(i, RandomCraftListHolder.getInstance().getNewReward(i, _rewardList));
				else
					holder.decLock();
			}
			_player.sendPacket(new ExCraftRandomInfo(_player));
		}
	}

	public void make()
	{
		if(_player.reduceAdena(Config.RANDOM_CRAFT_TRY_COMMISSION, true))
		{
			final int madeId = Rnd.get(0, 4);
			final RandomCraftRewardItem holder = _rewardList.get(madeId);
			final int itemId = holder.getItemId();
			final long itemCount = holder.getItemCount();
			final int itemEnchant = holder.getEnchant();
			_rewardList.clear();
			final ItemInstance item = _player.getInventory().addItem(itemId, itemCount);

			if(RandomCraftListHolder.getInstance().isAnnounce(itemId))
			{
				ExItemAnnounce _packet = new ExItemAnnounce(_player, item, ExItemAnnounce.RANDOM_CRAFT);
				for(Player st : GameObjectsStorage.getPlayers(true, false))
				{
					if(!st.getVarBoolean("DisableSysMsgRandom", false))
						st.sendPacket(_packet);
				}

				Log.LogItem(_player, "randomly crafted", item, item.getCount());
			}

			_player.getListeners().onPlayerRandomCraft(itemId, itemCount);
			_player.sendPacket(new ExCraftRandomMake(itemId, itemCount, itemEnchant));
			_player.sendPacket(new ExCraftRandomInfo(_player));

		}
	}

	public List<RandomCraftRewardItem> getRewards()
	{
		return _rewardList;
	}

	public int getFullCraftPoints()
	{
		return _craftPoints;
	}

	public void addFullCraftPoints(int value)
	{
		addFullCraftPoints(value, false);
	}

	public void addFullCraftPoints(int value, boolean broadcast)
	{
		_craftPoints = Math.min(_craftPoints + value, MAX_CRAFT_POINTS);

		if(value > 0)
			_isSayhaRoll = true;

		if(broadcast)
			_player.sendPacket(new ExCraftInfo(_player));
	}

	public void removeFullCraftPoints(int value)
	{
		_craftPoints -= value;
		_player.sendPacket(new ExCraftInfo(_player));
	}

	public void addExp(int add_exp)
	{
		int value = getExp() + add_exp;
		if(_craftPoints == MAX_CRAFT_POINTS && value >= MAX_EXP_POINTS)
			_exp = MAX_EXP_POINTS;
		else
		{
			if(value < MAX_EXP_POINTS)
				_exp = value;
			else
			{
				_exp = value % MAX_EXP_POINTS;
				addFullCraftPoints(value / MAX_EXP_POINTS);
			}
			_player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_ACQUIRED_S1_CRAFT_SCALE_POINTS).addNumber(add_exp));
		}
		_player.sendPacket(new ExCraftInfo(_player));
	}

	public int getExp()
	{
		return _exp;
	}

	public void setIsSayhaRoll(boolean value)
	{
		_isSayhaRoll = value;
	}

	public boolean isSayhaRoll()
	{
		return _isSayhaRoll;
	}

	public int getLockedSlotCount()
	{
		return (int) _rewardList.stream().filter(h -> h.isLocked()).count();
	}
}
