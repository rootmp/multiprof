package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.templates.StatsSet;

public class VipAttendanceDAO
{
	private static final Logger _log = LoggerFactory.getLogger(VipAttendanceDAO.class);
	private static final VipAttendanceDAO _instance = new VipAttendanceDAO();

	private static final String SELECT_SQL = "SELECT * FROM account_vip_attendance WHERE account_name=?";
	private static final String INSERT_SQL = "INSERT INTO account_vip_attendance (account_name, cAttendanceDay, cRewardDay, cFollowBaseDay, dateLastReward, expire_time) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE cAttendanceDay=VALUES(cAttendanceDay), cRewardDay=VALUES(cRewardDay), cFollowBaseDay=VALUES(cFollowBaseDay), dateLastReward=VALUES(dateLastReward), expire_time=VALUES(expire_time)";

	public static VipAttendanceDAO getInstance()
	{
		return _instance;
	}

	public void deleteExpiredRecords()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			long unixTime = Config.VIP_ATTENDANCE_REWARDS_START_DATE.atZone(ZoneId.systemDefault()).toEpochSecond();
			statement = con.prepareStatement("DELETE FROM account_vip_attendance WHERE expire_time <= ?");
			statement.setLong(1, unixTime);
			statement.executeUpdate();
		}
		catch(SQLException e)
		{
			_log.error("Error while deleting expired records from account_vip_attendance", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void deleteAllRecords()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM account_vip_attendance");
			statement.executeUpdate();
		}
		catch(SQLException e)
		{
			_log.error("Error while deleting all records from account_vip_attendance", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public StatsSet select(String account_name)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		StatsSet set = new StatsSet();
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL);
			statement.setString(1, account_name);

			rset = statement.executeQuery();

			if(rset.next())
			{
				set.put("account_name", rset.getString("account_name"));
				set.put("cAttendanceDay", rset.getInt("cAttendanceDay"));
				set.put("cRewardDay", rset.getInt("cRewardDay"));
				set.put("cFollowBaseDay", rset.getInt("cFollowBaseDay"));
				set.put("dateLastReward", rset.getInt("dateLastReward"));
				set.put("expire_time", rset.getLong("expire_time"));
			}
		}
		catch(SQLException e)
		{
			_log.error("Error while executing SQL query for account_name: " + account_name, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return set;
	}

	public void insert(StatsSet set)
	{
		if(set.isEmpty() || set.getString("account_name", "").isEmpty())
			return;
		long endTime = Config.VIP_ATTENDANCE_REWARDS_END_DATE.atZone(ZoneId.systemDefault()).toEpochSecond();
		
		Connection con = null;
		PreparedStatement statement = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();

			statement = con.prepareStatement(INSERT_SQL);
			statement.setString(1, set.getString("account_name"));
			statement.setInt(2, set.getInteger("cAttendanceDay", 0));
			statement.setInt(3, set.getInteger("cRewardDay", 0));
			statement.setInt(4, set.getInteger("cAttendanceDay", 0));//set.getInteger("cFollowBaseDay",0));
			statement.setInt(5, set.getInteger("dateLastReward", 0));
			statement.setLong(6, set.getLong("expire_time", endTime));
			statement.execute();
		}
		catch(SQLException e)
		{
			_log.error("Error while inserting character vip attendance data for account_name: " + set.getString("account_name"), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
