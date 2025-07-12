package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.enums.WorldExchangeItemStatusType;
import l2s.gameserver.enums.WorldExchangeItemSubType;
import l2s.gameserver.instancemanager.WorldExchangeManager;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.items.ItemInfo;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.templates.WorldExchangeHolder;

public class WorldExchangeDAO
{
	private static final Logger _log = LoggerFactory.getLogger(WorldExchangeDAO.class);
	private static final WorldExchangeDAO _instance = new WorldExchangeDAO();

	private static final String RESTORE_INFO = "SELECT * FROM world_exchange_items";
	private static final String INSERT_WORLD_EXCHANGE = "REPLACE INTO world_exchange_items (`world_exchange_id`, `item_object_id`, `item_status`, `category_id`, `price`, `old_owner_id`, `start_time`, `end_time`, `listing_type`, `currency_type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static WorldExchangeDAO getInstance()
	{
		return _instance;
	}

	public Map<Long, WorldExchangeHolder> select()
	{
		Map<Long, WorldExchangeHolder> result = new ConcurrentHashMap<>();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_INFO);
			rs = statement.executeQuery();
			while(rs.next())
			{
				boolean needChange = false;
				final long worldExchangeId = rs.getLong("world_exchange_id");
				final ItemInstance itemInstance = ItemsDAO.getInstance().load(rs.getInt("item_object_id"));
				if(itemInstance == null)
				{
					//_log.warn(getClass().getSimpleName() + ": Failed loading commission item with world exchange id " + worldExchangeId + " because item instance does not exist or failed to load.");
					continue;
				}

				WorldExchangeItemStatusType storeType = WorldExchangeItemStatusType.getWorldExchangeItemStatusType(rs.getInt("item_status"));
				if(storeType == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
					continue;

				final WorldExchangeItemSubType categoryId = WorldExchangeItemSubType.getWorldExchangeItemSubType(rs.getInt("category_id"));
				final long price = rs.getLong("price");
				final int bidPlayerObjectId = rs.getInt("old_owner_id");
				final long startTime = rs.getLong("start_time");
				long endTime = rs.getLong("end_time");
				final int listing_type = rs.getInt("listing_type");
				final int currency_type = rs.getInt("currency_type");
				
				if(endTime < System.currentTimeMillis())
				{
					if((storeType == WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME) || (storeType == WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD))
					{
						itemInstance.setLocation(ItemLocation.VOID);
						itemInstance.update();
						continue;
					}
					endTime = WorldExchangeManager.calculateDate(Config.WORLD_EXCHANGE_ITEM_BACK_PERIOD);
					storeType = WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME;
					needChange = true;
				}
				result.put(worldExchangeId, new WorldExchangeHolder(worldExchangeId, itemInstance, new ItemInfo(itemInstance), price, bidPlayerObjectId, storeType, categoryId, startTime, endTime, needChange, listing_type, currency_type));
			}
		}
		catch(Exception e)
		{
			_log.warn(getClass().getSimpleName() + ": Failed loading bid items.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
		return result;
	}

	public void storeMe(Map<Long, WorldExchangeHolder> _itemBids)
	{
		if(!Config.ENABLE_WORLD_EXCHANGE)
			return;

		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_WORLD_EXCHANGE);

			for(WorldExchangeHolder holder : _itemBids.values())
			{
				if(!holder.hasChanges())
					continue;

				statement.setLong(1, holder.getWorldExchangeId());
				statement.setLong(2, holder.getItemInstance().getObjectId());
				statement.setInt(3, holder.getStoreType().getId());
				statement.setInt(4, holder.getCategory().getId());
				statement.setLong(5, holder.getPrice());
				statement.setInt(6, holder.getOldOwnerId());
				statement.setLong(7, holder.getStartTime());
				statement.setLong(8, holder.getEndTime());
				statement.setInt(9, holder.getListingType());
				statement.setInt(10, holder.getCurrencyType());
				
				statement.addBatch();
			}
			statement.executeBatch();
		}
		catch(SQLException e)
		{
			_log.warn("Error while saving World Exchange item bids:\n", e);
		}		
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void insert(WorldExchangeHolder holder, long worldExchangeId)
	{
		//if(Config.WORLD_EXCHANGE_LAZY_UPDATE)
		// return; 
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_WORLD_EXCHANGE);
			
			statement.setLong(1, holder.getWorldExchangeId());
			statement.setLong(2, holder.getItemInstance().getObjectId());
			statement.setInt(3, holder.getStoreType().getId());
			statement.setInt(4, holder.getCategory().getId());
			statement.setLong(5, holder.getPrice());
			statement.setInt(6, holder.getOldOwnerId());
			statement.setString(7, String.valueOf(holder.getStartTime()));
			statement.setString(8, String.valueOf(holder.getEndTime()));
			statement.setInt(9, holder.getListingType());
			statement.setInt(10, holder.getCurrencyType());
			statement.execute();

		}
		catch(SQLException e)
		{
			_log.warn("Error while saving World Exchange item bid " + worldExchangeId + "\n", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}