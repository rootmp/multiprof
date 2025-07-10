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

	private static final String RESTORE_ITEM_ENSOUL = "SELECT type, id, ensoul_id FROM items_ensoul WHERE object_id = ?";
	private static final String REMOVE_ITEM_ENSOUL = "DELETE FROM items_ensoul WHERE object_id = ? AND type=? AND id=?";
	
	private static final String STORE_ITEM_ENSOUL = "INSERT INTO items_ensoul (`object_id`, `type`, `id` , `ensoul_id`) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE type = ?, id = ?, ensoul_id = ?";
	private static final String DELETE_ITEM_ENSOUL2 = "DELETE FROM items_ensoul WHERE object_id = ?";
	
	private static final ItemsEnsoulDAO instance = new ItemsEnsoulDAO();

	public static final ItemsEnsoulDAO getInstance()
	{
		return instance;
	}

	public void restore(ItemInstance item)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_ITEM_ENSOUL);
			statement.setInt(1, item.getObjectId());
			rset = statement.executeQuery();
			while(rset.next())
			{
				int type = rset.getInt("type");
				int id = rset.getInt("id");
				if(type != 1 && type != 2)
				{
					delete(item.getObjectId(), type, id);
					continue;
				}

				Ensoul ensoul = EnsoulHolder.getInstance().getEnsoul(rset.getInt("ensoul_id"));
				if(ensoul == null)
				{
					delete(item.getObjectId(), type, id);
					continue;
				}
				item.addSpecialAbility(ensoul, id, type, false);
			}
		}
		catch(Exception e)
		{
			_log.info("ItemsEnsoulDAO.restore(int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void delete(int objectId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_ITEM_ENSOUL2);
			statement.setInt(1, objectId);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.info("ItemsEnsoulDAO.delete(int,int,int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	public void delete(int objectId, int type, int id)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REMOVE_ITEM_ENSOUL);
			statement.setInt(1, objectId);
			statement.setInt(2, type);
			statement.setInt(3, id);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.info("ItemsEnsoulDAO.delete(int,int,int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void insert(int objectId, Ensoul[] _ensoulOptions, Ensoul[] _ensoulSpecialOptions)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(STORE_ITEM_ENSOUL);
			
			statement.setInt(1, objectId);
			for (int i = 0; i < _ensoulOptions.length; i++)
			{
				if (_ensoulOptions[i] == null)
				{
					continue;
				}
				
				statement.setInt(2, 1); // regular options
				statement.setInt(3, i);
				statement.setInt(4, _ensoulOptions[i].getId());
				
				statement.setInt(5, 1); // regular options
				statement.setInt(6, i);
				statement.setInt(7, _ensoulOptions[i].getId());
				statement.execute();
			}
			
			for (int i = 0; i < _ensoulSpecialOptions.length; i++)
			{
				if (_ensoulSpecialOptions[i] == null)
				{
					continue;
				}
				
				statement.setInt(2, 2); // special options
				statement.setInt(3, i);
				statement.setInt(4, _ensoulSpecialOptions[i].getId());
				
				statement.setInt(5, 2); // special options
				statement.setInt(6, i);
				statement.setInt(7, _ensoulSpecialOptions[i].getId());
				
				statement.execute();
			}
		}
		catch(Exception e)
		{
			_log.info("ItemsEnsoulDAO.insert(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}