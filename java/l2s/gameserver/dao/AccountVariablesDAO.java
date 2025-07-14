package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.templates.VariableContainer;
import l2s.gameserver.utils.Strings;

public class AccountVariablesDAO
{
	private static final Logger _log = LoggerFactory.getLogger(AccountVariablesDAO.class);
	private static final AccountVariablesDAO _instance = new AccountVariablesDAO();

	public static final String SELECT_SQL_QUERY = "SELECT var, value, expire_time FROM account_variables WHERE account_name = ?";
	public static final String DELETE_SQL_QUERY = "DELETE FROM account_variables WHERE account_name = ? AND var = ? LIMIT 1";
	public static final String DELETE_EXPIRED_SQL_QUERY = "DELETE FROM account_variables WHERE expire_time > 0 AND expire_time < ?";
	public static final String INSERT_SQL_QUERY = "REPLACE INTO account_variables (account_name, var, value, expire_time) VALUES (?,?,?,?)";
	public static final String DELETE_ALL_SQL_QUERY = "DELETE FROM account_variables WHERE var=?";

	public static final String DELETE_SQL_QUERY2 = "DELETE FROM account_variables WHERE account_name = ? AND var LIKE ?";
	public static final String DELETE_ALL_SQL_QUERY2 = "DELETE FROM account_variables WHERE var LIKE ?";

	public AccountVariablesDAO()
	{
		deleteExpiredVars();
	}

	public static AccountVariablesDAO getInstance()
	{
		return _instance;
	}

	public String select(String account, String _var)
	{
		return select(account, _var, null);
	}

	public String select(String account, String _var, String defaultVal)
	{
		String result_value = defaultVal;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT value FROM account_variables WHERE account_name=? AND var=?");
			statement.setString(1, account);
			statement.setString(2, _var);
			rset = statement.executeQuery();
			if(rset.next())
				result_value = rset.getString("value");
		}
		catch(Exception e)
		{
			_log.info("AccountVariablesDAO.select(String, String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return result_value;
	}

	private void deleteExpiredVars()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_EXPIRED_SQL_QUERY);
			statement.setLong(1, System.currentTimeMillis());
			statement.execute();
		}
		catch(final Exception e)
		{
			_log.error("AccountVariablesDAO:deleteExpiredVars()", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public boolean delete2(String acc, String varName)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY2);
			statement.setString(1, acc);
			statement.setString(2, varName);
			statement.execute();
		}
		catch(final Exception e)
		{
			_log.error("AccountVariablesDAO:delete(acc,varName)", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public boolean delete(String acc, String varName)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setString(1, acc);
			statement.setString(2, varName);
			statement.execute();
		}
		catch(final Exception e)
		{
			_log.error("AccountVariablesDAO:delete(acc,varName)", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public List<VariableContainer> restore(String acc)
	{
		List<VariableContainer> result = new ArrayList<VariableContainer>();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setString(1, acc);
			rset = statement.executeQuery();
			while(rset.next())
			{
				long expireTime = rset.getLong("expire_time");
				if(expireTime > 0 && expireTime < System.currentTimeMillis())
					continue;

				result.add(new VariableContainer(rset.getString("var"), Strings.stripSlashes(rset.getString("value")), expireTime));
			}
		}
		catch(Exception e)
		{
			_log.error("AccountVariablesDAO:restore(acc)", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return result;
	}

	public boolean insert(String acc, VariableContainer _var)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setString(1, acc);
			statement.setString(2, _var.getName());
			statement.setString(3, _var.getValue());
			statement.setLong(4, _var.getExpireTime());
			statement.executeUpdate();
		}
		catch(final Exception e)
		{
			_log.error("AccountVariablesDAO:insert(acc,var)", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public void insert(String account, String _var, String value)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO account_variables VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value=?");
			statement.setString(1, account);
			statement.setString(2, _var);
			statement.setString(3, value);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.info("AccountVariablesDAO.insert(String, String, String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void delete2(String _var)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_ALL_SQL_QUERY2);
			statement.setString(1, _var);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.info("AccountVariablesDAO.delete2(String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void delete(String _var)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_ALL_SQL_QUERY);
			statement.setString(1, _var);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.info("AccountVariablesDAO.delete(String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
