package l2s.authserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.authserver.database.DatabaseFactory;
import l2s.commons.ban.BanBindType;
import l2s.commons.ban.BanInfo;
import l2s.commons.dbutils.DbUtils;

/**
 * @author Bonux (Head Developer L2-scripts.com) 10.04.2019 Developed for
 *         L2-Scripts.com
 **/
public class AuthBansDAO
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthBansDAO.class);

	public static final String SELECT_SQL_QUERY = "SELECT bind_value, end_time, reason FROM auth_bans WHERE bind_type=?";
	public static final String SELECT_ACCESS_LEVEL_SQL_QUERY = "SELECT login, access_level, ban_expire FROM accounts";
	public static final String DELETE_SQL_QUERY = "DELETE FROM auth_bans WHERE bind_type=? AND bind_value=?";
	public static final String INSERT_SQL_QUERY = "REPLACE INTO auth_bans(bind_type, bind_value, end_time, reason) VALUES (?,?,?,?)";
	public static final String CLEAN_UP_SQL_QUERY = "DELETE FROM auth_bans WHERE end_time < ? OR bind_value = ''";
	public static final String CLEAN_UP_BY_TYPE_SQL_QUERY = "DELETE FROM auth_bans WHERE bind_type=?";

	public void select(Map<String, BanInfo> bans, BanBindType bindType)
	{
		if(!bindType.isAuth())
			return;

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setString(1, bindType.toString().toLowerCase());
			rset = statement.executeQuery();
			while(rset.next())
			{
				int endTime = rset.getInt("end_time");
				if(endTime != -1 && endTime < (System.currentTimeMillis() / 1000))
					continue;

				String bindValue = rset.getString("bind_value");
				if(StringUtils.isEmpty(bindValue))
					continue;

				String reason = rset.getString("reason");
				bans.put(bindValue, new BanInfo(endTime, reason));
			}

			if(bindType == BanBindType.LOGIN)
			{
				DbUtils.closeQuietly(statement, rset);

				statement = con.prepareStatement(SELECT_ACCESS_LEVEL_SQL_QUERY);
				rset = statement.executeQuery();
				while(rset.next())
				{
					int accessLevel = rset.getInt("access_level");
					if(accessLevel < 0)
						bans.put(rset.getString("login"), new BanInfo(Integer.MAX_VALUE, ""));
					else
					{
						int banExpire = rset.getInt("ban_expire");
						if(banExpire == -1 || banExpire > (System.currentTimeMillis() / 1000))
							bans.put(rset.getString("login"), new BanInfo(banExpire, ""));
					}
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.error("AuthBansDAO.select(Map,BanBindType): ", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public boolean insert(BanBindType bindType, String bindValue, BanInfo banInfo)
	{
		if(!bindType.isAuth())
			return false;

		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			PreparedStatement statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setString(1, bindType.toString().toLowerCase());
			statement.setString(2, bindValue);
			statement.setInt(3, banInfo.getEndTime());
			statement.setString(4, banInfo.getReason());
			statement.execute();
		}
		catch(Exception e)
		{
			LOGGER.error("AuthBansDAO.insert(BanBindType,String,BanInfo): ", e);
			return false;
		}

		return true;
	}

	public boolean delete(BanBindType bindType, String bindValue)
	{
		if(!bindType.isAuth())
			return false;

		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			PreparedStatement statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setString(1, bindType.toString().toLowerCase());
			statement.setString(2, bindValue);
			statement.execute();
		}
		catch(Exception e)
		{
			LOGGER.error("AuthBansDAO.delete(BanBindType,String): ", e);
			return false;
		}

		return true;
	}

	public void cleanUp()
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			PreparedStatement statement = con.prepareStatement(CLEAN_UP_SQL_QUERY);
			statement.setInt(1, (int) (System.currentTimeMillis() / 1000));
			statement.execute();

			for(BanBindType bindType : BanBindType.VALUES)
			{
				if(bindType.isAuth())
					continue;

				DbUtils.closeQuietly(statement);

				statement = con.prepareStatement(CLEAN_UP_BY_TYPE_SQL_QUERY);
				statement.setString(1, bindType.toString().toLowerCase());
				statement.execute();
			}
		}
		catch(Exception e)
		{
			LOGGER.error("AuthBansDAO.cleanUp(): ", e);
		}
	}

	private static final AuthBansDAO INSTANCE = new AuthBansDAO();

	public static AuthBansDAO getInstance()
	{
		return INSTANCE;
	}
}
