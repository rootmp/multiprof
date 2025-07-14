package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;

/**
 * @author nexvill
 */
public class AccountCollectionFavoritesDAO
{
	private static final Logger _log = LoggerFactory.getLogger(AccountCollectionFavoritesDAO.class);

	private static final AccountCollectionFavoritesDAO _instance = new AccountCollectionFavoritesDAO();

	private static final String INSERT_QUERY = "INSERT INTO account_collection_favorites (account_name,collection_id) VALUES(?,?)";
	private static final String SELECT_QUERY = "SELECT collection_id FROM account_collection_favorites WHERE account_name = ?";
	private static final String DELETE_QUERY = "DELETE FROM account_collection_favorites WHERE account_name=? AND collection_id=?";

	public static AccountCollectionFavoritesDAO getInstance()
	{
		return _instance;
	}

	public TIntSet restore(String account_name)
	{
		TIntSet result = new TIntHashSet();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setString(1, account_name);
			rset = statement.executeQuery();
			while(rset.next())
			{
				result.add(rset.getInt("collection_id"));
			}
		}
		catch(Exception e)
		{
			_log.error("CharacterCollectionFavoritesDAO.restore(int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return result;
	}

	public void insert(String account_name, int collectionId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_QUERY);
			statement.setString(1, account_name);
			statement.setInt(2, collectionId);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.warn(account_name + " could not add collection favorite id: " + collectionId, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void delete(String account_name, int collectionId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setString(1, account_name);
			statement.setInt(2, collectionId);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.warn(getClass().getSimpleName() + ": could not delete teleport id: " + collectionId + " account_name: " + account_name, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
