package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.data.xml.holder.CollectionsHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.item.data.CollectionItemData;

/**
 * @author nexvill
 */
public class CollectionsDAO
{
	private static final CollectionsDAO INSTANCE = new CollectionsDAO();

	public static CollectionsDAO getInstance()
	{
		return INSTANCE;
	}

	private static final String SELECT_QUERY = "SELECT tab_id, collection_id, item_id, slot_id FROM character_collections WHERE account=?";
	private static final String INSERT_QUERY = "INSERT INTO character_collections (account, tab_id, collection_id, item_id, slot_id) VALUES (?, ?, ?, ?, ?)";
	private static final String DELETE_QUERY = "DELETE FROM character_collections WHERE account=? AND collection_id=?";

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionsDAO.class);

	public void restore(Player owner, Map<Integer, List<CollectionTemplate>> collections)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setString(1, owner.getAccountName());
			rset = statement.executeQuery();

			while (rset.next())
			{
				int collectionId = rset.getInt("collection_id");
				CollectionTemplate template = CollectionsHolder.getInstance().getCollection(collectionId);
				if (template == null)
				{
					delete(owner, collectionId);
					continue;
				}

				int tabId = rset.getInt("tab_id");
				int itemId = rset.getInt("item_id");
				int slotId = rset.getInt("slot_id");
				CollectionTemplate collection = new CollectionTemplate(collectionId, tabId, 0);
				collection.addItem(new CollectionItemData(itemId, 0, 0, 0, slotId));

				collections.computeIfAbsent(collection.getId(), l -> new ArrayList<>()).add(collection);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("CollectionsDAO.select(Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public boolean insert(Player owner, CollectionTemplate template)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_QUERY);
			statement.setString(1, owner.getAccountName());
			statement.setInt(2, template.getTabId());
			statement.setInt(3, template.getId());
			statement.setInt(4, template.getItems().get(0).getId());
			statement.setInt(5, template.getItems().get(0).getSlotId());
			statement.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("CollectionsDAO.insert(Player,CollectionTemplate): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public boolean delete(Player owner, int collection_id)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setString(1, owner.getAccountName());
			statement.setInt(2, collection_id);
			statement.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("CollectionsDAO.delete(Player,int): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
}
