package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.items.ItemInstance;

public class ItemsToRestoreDAO
{
	private static final Logger _log = LoggerFactory.getLogger(ItemsToRestoreDAO.class);

	private final static String RESTORE_ITEM = "SELECT object_id, owner_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, life_time, custom_flags, variation_stone_id, variation1_id, variation2_id, agathion_energy, appearance_stone_id, visual_id, isBlessed, lost_date FROM items_to_restore WHERE object_id = ?";
	private final static String RESTORE_OWNER_ITEMS = "SELECT object_id FROM items_to_restore WHERE owner_id = ?";
	private final static String STORE_ITEM = "INSERT INTO items_to_restore (object_id, owner_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, life_time, custom_flags, variation_stone_id, variation1_id, variation2_id, agathion_energy, appearance_stone_id, visual_id, isBlessed, lost_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final static String REMOVE_ITEM = "DELETE FROM items_to_restore WHERE object_id = ?";

	private final static ItemsToRestoreDAO instance = new ItemsToRestoreDAO();

	public final static ItemsToRestoreDAO getInstance()
	{
		return instance;
	}

	private ItemInstance load0(int objectId) throws SQLException
	{
		ItemInstance item = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_ITEM);
			statement.setInt(1, objectId);
			rset = statement.executeQuery();
			item = load0(rset);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return item;
	}

	private ItemInstance load0(ResultSet rset) throws SQLException
	{
		ItemInstance item = null;

		if (rset.next())
		{
			final int objectId = rset.getInt(1);
			item = new ItemInstance(objectId);
			item.setOwnerId(rset.getInt(2));
			item.setItemId(rset.getInt(3));
			item.setCount(rset.getLong(4));
			item.setEnchantLevel(rset.getInt(5));
			item.setLocName(rset.getString(6));
			item.setLocData(rset.getInt(7));
			item.setCustomType1(rset.getInt(8));
			item.setCustomType2(rset.getInt(9));
			item.setLifeTime(rset.getInt(10));
			item.setCustomFlags(rset.getInt(11));
			item.setVariationStoneId(rset.getInt(12));
			item.setVariation1Id(rset.getInt(13));
			item.setVariation2Id(rset.getInt(14));
			item.setAgathionEnergy(rset.getInt(15));
			item.setAppearanceStoneId(rset.getInt(16));
			item.setVisualId(rset.getInt(17));
			item.setBlessed(rset.getBoolean(18));
			item.setLostDate(rset.getInt(19));
			item.restoreEnsoul(true);
		}

		return item;
	}

	private void save0(ItemInstance item, PreparedStatement statement) throws SQLException
	{
		statement.setInt(1, item.getObjectId());
		statement.setInt(2, item.getOwnerId());
		statement.setInt(3, item.getItemId());
		statement.setLong(4, item.getCount());
		statement.setInt(5, item.getEnchantLevel());
		statement.setString(6, item.getLocName());
		statement.setInt(7, item.getLocData());
		statement.setInt(8, item.getCustomType1());
		statement.setInt(9, item.getCustomType2());
		statement.setInt(10, item.getLifeTime());
		statement.setInt(11, item.getCustomFlags());
		statement.setInt(12, item.getVariationStoneId());
		statement.setInt(13, item.getVariation1Id());
		statement.setInt(14, item.getVariation2Id());
		statement.setInt(15, item.getAgathionEnergy());
		statement.setInt(16, item.getAppearanceStoneId());
		statement.setInt(17, item.getVisualId());
		statement.setBoolean(18, item.isBlessed());
		statement.setInt(19, item.getLostDate());
	}

	private void save0(ItemInstance item) throws SQLException
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(STORE_ITEM);
			save0(item, statement);
			statement.execute();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	private void delete0(ItemInstance item, PreparedStatement statement) throws SQLException
	{
		statement.setInt(1, item.getOwnerId());
	}

	private void delete0(ItemInstance item) throws SQLException
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REMOVE_ITEM);
			delete0(item, statement);
			statement.execute();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public ItemInstance load(Integer objectId)
	{
		ItemInstance item;
		try
		{
			item = load0(objectId);
			if (item == null)
				return null;
		}
		catch (final SQLException e)
		{
			_log.error("Error while load item to restore : " + objectId, e);
			return null;
		}

		return item;
	}

	public Collection<ItemInstance> load(Collection<Integer> objectIds)
	{
		Collection<ItemInstance> list = Collections.emptyList();

		if (objectIds.isEmpty())
			return list;

		list = new ArrayList<ItemInstance>(objectIds.size());

		ItemInstance item;
		for (final Integer objectId : objectIds)
		{
			item = load(objectId);
			if (item != null)
			{
				list.add(item);
			}
		}

		return list;
	}

	public void save(ItemInstance item)
	{
		try
		{
			save0(item);
		}
		catch (final SQLException e)
		{
			_log.error("Error while saving item to restore : " + item, e);
			return;
		}
	}

	public void save(Collection<ItemInstance> items)
	{
		if (items.isEmpty())
			return;

		for (final ItemInstance item : items)
		{
			save(item);
		}
	}

	public void delete(ItemInstance item)
	{
		try
		{
			delete0(item);
		}
		catch (final SQLException e)
		{
			_log.error("Error while deleting item to restore : " + item, e);
			return;
		}
	}

	public void delete(Collection<ItemInstance> items)
	{
		if (items.isEmpty())
			return;

		for (final ItemInstance item : items)
		{
			delete(item);
		}
	}

	public Collection<ItemInstance> getItemsByOwnerId(int ownerId)
	{
		Collection<Integer> objectIds = Collections.emptyList();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_OWNER_ITEMS);
			statement.setInt(1, ownerId);
			rset = statement.executeQuery();
			objectIds = new ArrayList<Integer>();
			while (rset.next())
			{
				objectIds.add(rset.getInt(1));
			}
		}
		catch (final SQLException e)
		{
			_log.error("Error while load items to restore of owner : " + ownerId, e);
			objectIds.clear();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return load(objectIds);
	}
}
