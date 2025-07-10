package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.items.PrivateStoreHistoryItem;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 23.12.2021
 **/
public class PrivateStoreHistoryDAO
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PrivateStoreHistoryDAO.class);

	private static final PrivateStoreHistoryDAO INSTANCE = new PrivateStoreHistoryDAO();

	public static PrivateStoreHistoryDAO getInstance()
	{
		return INSTANCE;
	}

	private static final String SELECT_SQL_QUERY = "SELECT store_type, time, item_id, count, enchant, price FROM private_store_history WHERE time >= ? AND time < ?";
	private static final String INSERT_SQL_QUERY = "INSERT INTO private_store_history (store_type,time,item_id,count,enchant,price) VALUES (?,?,?,?,?,?)";
	private static final String DELETE_EXPIRED_SQL_QUERY = "DELETE FROM private_store_history WHERE time < ?";

	public PrivateStoreHistoryDAO()
	{
		//
	}

	public boolean select(long minTime, long maxTime, List<PrivateStoreHistoryItem> list)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			int i = 0;
			statement.setInt(++i, (int) TimeUnit.MILLISECONDS.toSeconds(minTime));
			statement.setInt(++i, (int) TimeUnit.MILLISECONDS.toSeconds(maxTime));
			rset = statement.executeQuery();
			while (rset.next())
			{
				PrivateStoreHistoryItem historyItem = new PrivateStoreHistoryItem(rset.getInt("item_id"), rset.getInt("store_type"), rset.getInt("time"));
				historyItem.setCount(rset.getLong("count"));
				historyItem.setEnchantLevel(rset.getInt("enchant"));
				historyItem.setPrice(rset.getLong("price"));
				list.add(historyItem);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("PrivateStoreHistoryDAO.restore(long,long,List): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return true;
	}

	public boolean insert(PrivateStoreHistoryItem historyItem)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			int i = 0;
			statement.setInt(++i, historyItem.getStoreType());
			statement.setInt(++i, historyItem.getTime());
			statement.setInt(++i, historyItem.getItemId());
			statement.setLong(++i, historyItem.getCount());
			statement.setInt(++i, historyItem.getEnchantLevel());
			statement.setLong(++i, historyItem.getPrice());
			statement.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("PrivateStoreHistoryDAO.insert(PrivateStoreHistoryItem): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public void deleteExpired(long time)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_EXPIRED_SQL_QUERY);
			int i = 0;
			statement.setInt(++i, (int) TimeUnit.MILLISECONDS.toSeconds(time));
			statement.execute();
		}
		catch (final Exception e)
		{
			LOGGER.error("PrivateStoreHistoryDAO:deleteExpired(long)", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
