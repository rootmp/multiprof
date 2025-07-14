package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.MissionLevelReward;

/**
 * @author nexvill
 */
public class CharacterMissionLevelRewardDAO
{
	private static final Logger _log = LoggerFactory.getLogger(CharacterMissionLevelRewardDAO.class);

	private static final CharacterMissionLevelRewardDAO _instance = new CharacterMissionLevelRewardDAO();

	private static final String SELECT_QUERY = "SELECT level, points, last_taken_basic, last_taken_additional, last_taken_bonus, taken_final FROM character_mission_level_reward WHERE char_id = ?";
	private static final String REPLACE_QUERY = "REPLACE INTO character_mission_level_reward (char_id,level,points,last_taken_basic,last_taken_additional,last_taken_bonus,taken_final) VALUES(?,?,?,?,?,?,?)";
	private static final String DELETE_QUERY = "DELETE FROM character_mission_level_reward WHERE char_id=?";

	public static CharacterMissionLevelRewardDAO getInstance()
	{
		return _instance;
	}

	public void restore(Player owner)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setInt(1, owner.getObjectId());
			rset = statement.executeQuery();
			MissionLevelReward info = owner.getMissionLevelReward();
			if(rset.next())
			{
				info.setLevel(rset.getInt("level"));
				info.setPoints(rset.getInt("points"));
				info.setLastTakenBasic(rset.getInt("last_taken_basic"));
				info.setLastTakenAdditional(rset.getInt("last_taken_additional"));
				info.setLastTakenBonus(rset.getInt("last_taken_bonus"));
				info.setTakenFinal(rset.getInt("taken_final") > 0);
			}
			else
			{
				info.setLevel(0);
				info.setPoints(0);
				info.setLastTakenBasic(0);
				info.setLastTakenAdditional(0);
				info.setLastTakenBonus(0);
				info.setTakenFinal(false);
			}
		}
		catch(Exception e)
		{
			_log.error("CharacterDailyMissionsDAO.select(Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public boolean store(Player owner, int level, int points, int lastTakenBasic, int lastTakenAdditional, int lastTakenBonus, boolean takenFinal)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REPLACE_QUERY);
			statement.setInt(1, owner.getObjectId());
			statement.setInt(2, level);
			statement.setInt(3, points);
			statement.setInt(4, lastTakenBasic);
			statement.setInt(5, lastTakenAdditional);
			statement.setInt(6, lastTakenBonus);
			statement.setInt(7, takenFinal ? 1 : 0);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.warn(owner.getDailyMissionList() + " could not store mission level reward info for character: " + owner.getName() + " : ", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}

	public boolean delete(Player owner)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setInt(1, owner.getObjectId());
			statement.execute();
		}
		catch(Exception e)
		{
			_log.warn(owner.getDailyMissionList() + " could not delete mission level reward data: OWNER_ID[" + owner.getObjectId() + "]:", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
}
