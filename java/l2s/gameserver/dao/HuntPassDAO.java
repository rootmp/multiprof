package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.HuntPass;

public class HuntPassDAO
{
	private static final Logger _log = LoggerFactory.getLogger(HuntPassDAO.class);
	private static final HuntPassDAO _instance = new HuntPassDAO();

	public static final String INSERT_HUNTPASS = "REPLACE INTO huntpass (`account_name`, `current_step`, `points`, `reward_step`, `is_paytowin`, `premium_reward_step`, `sayha_points_available`, `sayha_points_used`, `unclaimed_reward`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String RESTORE_HUNTPASS = "SELECT * FROM huntpass WHERE account_name=?";
	public static final String UPDATE_HUNTPASS = "UPDATE huntpass SET current_step = 0, reward_step = 0, premium_reward_step = 0, sayha_points_available = 0, sayha_points_used = 0";

	public static HuntPassDAO getInstance()
	{
		return _instance;
	}

	public void store(HuntPass huntPass)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_HUNTPASS);
			statement.setString(1, huntPass.getAccountName());
			statement.setInt(2, huntPass.getCurrentStep());
			statement.setInt(3, huntPass.getPoints());
			statement.setInt(4, huntPass.getRewardStep());
			statement.setBoolean(5, huntPass.isPremium());
			statement.setInt(6, huntPass.getPremiumRewardStep());
			statement.setInt(7, huntPass.getAvailableSayhaTime());
			statement.setInt(8, huntPass.getUsedSayhaTime());
			statement.setBoolean(9, huntPass.rewardAlert());
			statement.execute();
		}
		catch(SQLException e)
		{
			_log.error("Could not store HuntPass data for Account " + huntPass.getAccountName(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void restore(HuntPass huntPass)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_HUNTPASS);
			statement.setString(1, huntPass.getAccountName());
			rset = statement.executeQuery();
			if(rset.next())
			{
				huntPass.setPoints(rset.getInt("points"));
				huntPass.setCurrentStep(rset.getInt("current_step"));
				huntPass.setRewardStep(rset.getInt("reward_step"));
				huntPass.setPremium(rset.getBoolean("is_paytowin"));
				huntPass.setPremiumRewardStep(rset.getInt("premium_reward_step"));
				huntPass.setAvailableSayhaTime(rset.getInt("sayha_points_available"));
				huntPass.setUsedSayhaTime(rset.getInt("sayha_points_used"));
				huntPass.setRewardAlert(rset.getBoolean("unclaimed_reward"));
			}
		}
		catch(SQLException e)
		{
			_log.error("Could not restore HuntPass for account: " + huntPass.getAccountName(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void delete()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_HUNTPASS);
			statement.execute();
		}
		catch(SQLException e)
		{
			_log.warn("HuntPass: Can't clear steps", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
