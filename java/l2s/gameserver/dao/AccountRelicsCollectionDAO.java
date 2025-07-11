package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.relics.CollectionRelicsInfo;

public class AccountRelicsCollectionDAO
{
	private static final AccountRelicsCollectionDAO INSTANCE = new AccountRelicsCollectionDAO();

	public static AccountRelicsCollectionDAO getInstance()
	{
		return INSTANCE;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRelicsCollectionDAO.class);

	private static final String SELECT_QUERY = "SELECT collection_id, relic_id, level FROM account_relics_collections WHERE account_name=?";
	private static final String SAVE_OR_UPDATE_QUERY = "INSERT INTO account_relics_collections (account_name, collection_id, relic_id, level) VALUES (?, ?, ?, ?) "
			+
			"ON DUPLICATE KEY UPDATE level=VALUES(level)";
	private static final String DELETE_QUERY = "DELETE FROM account_relics_collections WHERE account_name=? AND collection_id=? AND relic_id=?";

	public Map<Integer, List<CollectionRelicsInfo>> restore(Player owner)
	{
		Map<Integer, List<CollectionRelicsInfo>> collectionsMap = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setString(1, owner.getAccountName());
			rs = statement.executeQuery();
			while(rs.next())
			{
				int collectionId = rs.getInt("collection_id");
				int relicId = rs.getInt("relic_id");
				int level = rs.getInt("level");
				CollectionRelicsInfo relicInfo = new CollectionRelicsInfo(relicId, level);

				collectionsMap.computeIfAbsent(collectionId, k -> new ArrayList<>()).add(relicInfo);
			}
		}
		catch(Exception e)
		{
			LOGGER.error("AccountRelicsCollectionDAO.restore(Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
		return collectionsMap;
	}

	public void saveOrUpdate(Player owner, int collectionId, CollectionRelicsInfo relicInfo)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SAVE_OR_UPDATE_QUERY);
			statement.setString(1, owner.getAccountName());
			statement.setInt(2, collectionId);
			statement.setInt(3, relicInfo.nRelicsID);
			statement.setInt(4, relicInfo.nLevel);
			statement.executeUpdate();
		}
		catch(Exception e)
		{
			LOGGER.error("AccountRelicsCollectionDAO.saveOrUpdate(Player, int, CollectionRelicsInfo): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void delete(Player owner, int collectionId, int relicId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setString(1, owner.getAccountName());
			statement.setInt(2, collectionId);
			statement.setInt(3, relicId);
			statement.executeUpdate();
		}
		catch(Exception e)
		{
			LOGGER.error("AccountRelicsCollectionDAO.delete(Player, int, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
