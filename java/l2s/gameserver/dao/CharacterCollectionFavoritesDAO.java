package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

/**
 * @author nexvill
 */
public class CharacterCollectionFavoritesDAO
{
	private static final Logger _log = LoggerFactory.getLogger(CharacterCollectionFavoritesDAO.class);

	private static final CharacterCollectionFavoritesDAO _instance = new CharacterCollectionFavoritesDAO();

	private static final String INSERT_QUERY = "INSERT INTO character_collection_favorites (char_id,collection_id) VALUES(?,?)";
	private static final String SELECT_QUERY = "SELECT collection_id FROM character_collection_favorites WHERE char_id = ?";
	private static final String DELETE_QUERY = "DELETE FROM character_collection_favorites WHERE char_id=? AND collection_id=?";

	public static CharacterCollectionFavoritesDAO getInstance()
	{
		return _instance;
	}

	public TIntSet restore(int objectId)
	{
		TIntSet result = new TIntHashSet();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setInt(1, objectId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				result.add(rset.getInt("collection_id"));
			}
		}
		catch (Exception e)
		{
			_log.error("CharacterCollectionFavoritesDAO.restore(int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return result;
	}

	public void insert(Player owner, int collectionId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_QUERY);
			statement.setInt(1, owner.getObjectId());
			statement.setInt(2, collectionId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn(owner.getFriendList() + " could not add collection favorite id: " + collectionId, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void delete(int ownerId, int collectionId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setInt(1, ownerId);
			statement.setInt(2, collectionId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn(getClass().getSimpleName() + ": could not delete teleport id: " + collectionId + " ownerId: " + ownerId, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
