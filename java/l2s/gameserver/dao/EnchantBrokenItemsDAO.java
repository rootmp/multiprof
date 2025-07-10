package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.items.EnchantBrokenItem;

public class EnchantBrokenItemsDAO
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ClanHallDAO.class);

	private static final EnchantBrokenItemsDAO INSTANCE = new EnchantBrokenItemsDAO();

	public static EnchantBrokenItemsDAO getInstance()
	{
		return INSTANCE;
	}

	private static final String SELECT_SQL_QUERY = "SELECT * FROM enchant_broken_items WHERE char_id=?";
	private static final String INSERT_SQL_QUERY = "INSERT INTO enchant_broken_items(char_id, item_id, enchant, time) VALUES (?,?,?,?)";
	private static final String DELETE_SQL_QUERY = "DELETE FROM enchant_broken_items WHERE char_id=? AND item_id=? AND enchant=? AND time=?";

	public void select(int ownerId, List<EnchantBrokenItem> items)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, ownerId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				int itemId = rset.getInt("item_id");
				int enchant = rset.getInt("enchant");
				int time = rset.getInt("time");
				items.add(new EnchantBrokenItem(itemId, enchant, time));
			}
		}
		catch (Exception e)
		{
			LOGGER.error("EnchantBrokenItemsDAO.select(int,List): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public boolean insert(int ownerId, EnchantBrokenItem item)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			int i = 0;
			statement.setInt(++i, ownerId);
			statement.setInt(++i, item.getId());
			statement.setInt(++i, item.getEnchant());
			statement.setInt(++i, item.getTime());
			statement.execute();
			return true;
		}
		catch (Exception e)
		{
			LOGGER.error("EnchantBrokenItemsDAO.insert(int,EnchantBrokenItem): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return false;
	}

	public boolean delete(int ownerId, EnchantBrokenItem item)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			int i = 0;
			statement.setInt(++i, ownerId);
			statement.setInt(++i, item.getId());
			statement.setInt(++i, item.getEnchant());
			statement.setInt(++i, item.getTime());
			statement.execute();
			return true;
		}
		catch (Exception e)
		{
			LOGGER.error("EnchantBrokenItemsDAO.delete(int,EnchantBrokenItem): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return false;
	}
}
