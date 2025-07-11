package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;

/**
 * @author nexvill
 */
public class PvpRankingDAO
{
	private static final Logger _log = LoggerFactory.getLogger(PvpRankingDAO.class);
	private static final PvpRankingDAO _instance = new PvpRankingDAO();

	public static final String INSERT_QUERY = "INSERT INTO pvp_ranking_data (obj_Id, kills, deaths, points, week) VALUES (?,?,?,?,?)";
	public static final String REPLACE_QUERY = "REPLACE INTO pvp_ranking_data (obj_Id, kills, deaths, points, week) VALUES (?,?,?,?,?)";

	public static final String INSERT_OR_UPDATE_QUERY = "INSERT INTO pvp_ranking_data (obj_Id, kills, deaths, points, week) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE kills=VALUES(kills), deaths=VALUES(deaths), points=VALUES(points)";
	public static final String UPDATE_QUERY = "UPDATE pvp_ranking_data SET kills=?, deaths=?, points=? WHERE obj_id=? AND week=0";
	public static final String UPDATE_WEEK_QUERY = "UPDATE pvp_ranking_data SET week=1 WHERE week=0";

	public static PvpRankingDAO getInstance()
	{
		return _instance;
	}

	public void insert(int playerObjId, int kills, int deaths, int points)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REPLACE_QUERY);
			statement.setInt(1, playerObjId);
			statement.setInt(2, kills);
			statement.setInt(3, deaths);
			statement.setInt(4, points);
			statement.setInt(5, 0);
			statement.execute();
		}
		catch(final Exception e)
		{
			_log.error("PvpRankingDAO:insert(playerObjId,kills,deaths,points)", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void update(int playerObjId, int kills, int deaths, int points)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_QUERY);
			statement.setInt(1, kills);
			statement.setInt(2, deaths);
			statement.setInt(3, points);
			statement.setInt(4, playerObjId);
			statement.execute();
		}
		catch(final Exception e)
		{
			_log.error("PvpRankingDAO:update(playerObjId,kills,deaths,points)", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void updateWeek()
	{
		Connection con = null;
		PreparedStatement deleteStatement = null;
		PreparedStatement updateStatement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			// Удаляем потенциальные конфликты
			deleteStatement = con.prepareStatement("DELETE FROM pvp_ranking_data WHERE week = 1 AND obj_Id IN (SELECT obj_Id FROM (SELECT obj_Id FROM pvp_ranking_data WHERE week = 0) AS tmp)");
			deleteStatement.executeUpdate();
			DbUtils.closeQuietly(deleteStatement);

			// Обновляем оставшиеся записи
			updateStatement = con.prepareStatement(UPDATE_WEEK_QUERY);
			updateStatement.executeUpdate();
		}
		catch(final Exception e)
		{
			_log.error("PvpRankingDAO:updateWeek()", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, deleteStatement);
			DbUtils.closeQuietly(updateStatement);
		}
	}
}
