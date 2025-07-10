package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.data.xml.holder.EnsoulHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.templates.item.support.Ensoul;

/**
 * @author Bonux
 **/
public class ItemsEnsoulDAO
{
	private static final Logger _log = LoggerFactory.getLogger(ItemsEnsoulDAO.class);

	private final static String RESTORE_ITEM_ENSOUL = "SELECT type, id, ensoul_id FROM items_ensoul WHERE object_id = ?";
	private final static String REMOVE_ITEM_ENSOUL = "DELETE FROM items_ensoul WHERE object_id = ? AND type=? AND id=?";
	private final static String STORE_ITEM_ENSOUL = "REPLACE INTO items_ensoul (object_id, type, id, ensoul_id) VALUES (?,?,?,?)";
	private final static String DELETE_ITEM_ENSOUL = "DELETE FROM items_ensoul WHERE object_id = ?";

	private final static String RESTORE_ITEM_RESTORE_ENSOUL = "SELECT type, id, ensoul_id FROM items_to_restore_ensoul WHERE object_id = ?";
	private final static String REMOVE_ITEM_RESTORE_ENSOUL = "DELETE FROM items_to_restore_ensoul WHERE object_id = ? AND type=? AND id=?";
	private final static String STORE_ITEM_RESTORE_ENSOUL = "REPLACE INTO items_to_restore_ensoul (object_id, type, id, ensoul_id) VALUES (?,?,?,?)";
	private final static String DELETE_ITEM_RESTORE_ENSOUL = "DELETE FROM items_to_restore_ensoul WHERE object_id = ?";

	private final static ItemsEnsoulDAO instance = new ItemsEnsoulDAO();

	public final static ItemsEnsoulDAO getInstance()
	{
		return instance;
	}

	public void restore(ItemInstance item, boolean toRestore)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if (!toRestore)
				statement = con.prepareStatement(RESTORE_ITEM_ENSOUL);
			else
				statement = con.prepareStatement(RESTORE_ITEM_RESTORE_ENSOUL);
			statement.setInt(1, item.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int type = rset.getInt("type");
				int id = rset.getInt("id");
				if (type != 1 && type != 2)
				{
					delete(item.getObjectId(), type, id, toRestore);
					continue;
				}

				Ensoul ensoul = EnsoulHolder.getInstance().getEnsoul(rset.getInt("ensoul_id"));
				if (ensoul == null)
				{
					delete(item.getObjectId(), type, id, toRestore);
					continue;
				}

				item.addEnsoul(type, id, ensoul, false);
			}
		}
		catch (Exception e)
		{
			_log.info("ItemsEnsoulDAO.restore(int,boolean): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void delete(int objectId, int type, int id, boolean toRestore)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if (!toRestore)
				statement = con.prepareStatement(REMOVE_ITEM_ENSOUL);
			else
				statement = con.prepareStatement(REMOVE_ITEM_RESTORE_ENSOUL);
			statement.setInt(1, objectId);
			statement.setInt(2, type);
			statement.setInt(3, id);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("ItemsEnsoulDAO.delete(int,int,int,boolean): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void delete(int objectId, boolean toRestore)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if (!toRestore)
				statement = con.prepareStatement(DELETE_ITEM_ENSOUL);
			else
				statement = con.prepareStatement(DELETE_ITEM_RESTORE_ENSOUL);
			statement.setInt(1, objectId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("ItemsEnsoulDAO.delete(int,boolean): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void insert(int objectId, int type, int id, int ensoulId, boolean toRestore)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if (!toRestore)
				statement = con.prepareStatement(STORE_ITEM_ENSOUL);
			else
				statement = con.prepareStatement(STORE_ITEM_RESTORE_ENSOUL);
			statement.setInt(1, objectId);
			statement.setInt(2, type);
			statement.setInt(3, id);
			statement.setInt(4, ensoulId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("ItemsEnsoulDAO.insert(int,int,int,int,boolean): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}