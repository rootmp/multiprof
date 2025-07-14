package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.ranking.OlympiadRankInfo;
import l2s.gameserver.templates.ranking.PkRankerData;
import l2s.gameserver.templates.ranking.PkRankerScoreData;

public class RankingDAO
{
	private static final Logger _log = LoggerFactory.getLogger(RankingDAO.class);
	private static final RankingDAO _instance = new RankingDAO();

	private static final String SQL_DELETE_ALL_PLAYER_RANKS_SNAPSHOT = "TRUNCATE TABLE ranking_main_snapshot";
	private static final String SQL_INSERT_PLAYER_RANK_SNAPSHOT = "INSERT INTO ranking_main_snapshot (`rank`, charId, name, level, classId, race, clanName, raceRank, classRank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_ALL_PLAYER_RANKS_SNAPSHOT = "SELECT * FROM ranking_main_snapshot";

	private static final String SQL_DELETE_ALL_PLAYER_RANKS = "TRUNCATE TABLE ranking_main";
	private static final String SQL_INSERT_PLAYER_RANK = "INSERT INTO ranking_main (`rank`, charId, name, level, classId, race, clanName, raceRank, classRank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_ALL_PLAYER_RANKS = "SELECT `rank`, charId, name, level, classId, race, clanName, raceRank, classRank FROM ranking_main";

	private static final String SQL_DELETE_ALL_PET_RANKS = "TRUNCATE TABLE ranking_pet";
	private static final String SQL_DELETE_ALL_PET_RANKS_SNAPSHOT = "TRUNCATE TABLE ranking_pet_snapshot";

	private static final String SQL_INSERT_PET_RANK = "INSERT INTO ranking_pet (objId, player_obj_Id, item_obj_id, player_name, clan_name, user_race, user_level, pet_index, nick_name, exp, sp, fed, pet_level, max_fed, npc_class_id, evolve_level, random_names, passive_skill, passive_skill_level, pet_index_rank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_INSERT_PET_RANK_SNAPSHOT = "INSERT INTO ranking_pet_snapshot (objId, player_obj_Id, item_obj_id, player_name, clan_name, user_race, user_level, pet_index, nick_name, exp, sp, fed, pet_level, max_fed, npc_class_id, evolve_level, random_names, passive_skill, passive_skill_level, pet_index_rank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_SELECT_ALL_PET_RANKS = "SELECT * FROM ranking_pet";
	private static final String SQL_SELECT_ALL_PET_RANKS_SNAPSHOT = "SELECT * FROM ranking_pet_snapshot";

	private static final String SQL_DELETE_ALL_OLYMPIAD_RANKS = "TRUNCATE TABLE ranking_olympiad";
	private static final String SQL_INSERT_OLYMPIAD_RANK = "INSERT INTO ranking_olympiad (charId, name, clanName, level, classId, clanLevel, competitions_win, competitions_loose, olympiad_points) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_ALL_OLYMPIAD_RANKS = "SELECT o.*, c.sex FROM ranking_olympiad o LEFT JOIN characters c ON o.charId = c.obj_Id";

	private static final String SQL_DELETE_ALL_RANKS_SCORE = "TRUNCATE TABLE ranking_score";
	private static final String SQL_INSERT_RANKS_SCORE = "INSERT INTO ranking_score (nRank, nCharId, sUserName, sPledgeName, nLevel, nClass, nRace, nRaceRank, nClassRank, nScore) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_ALL_RANKS_SCORE = "SELECT * FROM ranking_score";

	private static final String SQL_DELETE_ALL_RANKS_SCORE_SNAPSHOT = "TRUNCATE TABLE ranking_score_snapshot";
	private static final String SQL_INSERT_RANKS_SCORE_SNAPSHOT = "INSERT INTO ranking_score_snapshot (nRank, nCharId, sUserName, sPledgeName, nLevel, nClass, nRace, nRaceRank, nClassRank, nScore) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_ALL_RANKS_SCORE_SNAPSHOT = "SELECT * FROM ranking_score_snapshot";

	public static RankingDAO getInstance()
	{
		return _instance;
	}

	public Map<Integer, PkRankerScoreData> loadRanksScore()
	{
		return loadRanksScore(SQL_SELECT_ALL_RANKS_SCORE);
	}

	public Map<Integer, PkRankerScoreData> loadRanksScoreSnapshot()
	{
		return loadRanksScore(SQL_SELECT_ALL_RANKS_SCORE_SNAPSHOT);
	}

	private Map<Integer, PkRankerScoreData> loadRanksScore(String sql_select)
	{
		Connection con = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		Map<Integer, PkRankerScoreData> scoreMap = new HashMap<>();

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			selectStatement = con.prepareStatement(sql_select);
			resultSet = selectStatement.executeQuery();

			while(resultSet.next())
			{
				int serverRank = resultSet.getInt("nRank");
				int charId = resultSet.getInt("nCharId");
				String userName = resultSet.getString("sUserName");
				String pledgeName = resultSet.getString("sPledgeName");
				int level = resultSet.getInt("nLevel");
				int classId = resultSet.getInt("nClass");
				int race = resultSet.getInt("nRace");
				int classRank = resultSet.getInt("nClassRank");
				int raceRank = resultSet.getInt("nRaceRank");
				int score = resultSet.getInt("nScore");

				scoreMap.put(serverRank, new PkRankerScoreData(charId, userName, pledgeName, level, classId, race, classRank, raceRank, serverRank, score));
			}
		}
		catch(SQLException e)
		{
			_log.error("Failed to load rank score data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, selectStatement, resultSet);
		}

		return scoreMap;
	}

	public void saveRanksScore(Map<Integer, PkRankerScoreData> scoreMap)
	{
		saveRanksScore(scoreMap, SQL_DELETE_ALL_RANKS_SCORE, SQL_INSERT_RANKS_SCORE);
	}

	public ConcurrentHashMap<Integer, PkRankerScoreData> saveRanksScoreSnapshot(Map<Integer, PkRankerScoreData> scoreMap)
	{
		saveRanksScore(scoreMap, SQL_DELETE_ALL_RANKS_SCORE_SNAPSHOT, SQL_INSERT_RANKS_SCORE_SNAPSHOT);
		return new ConcurrentHashMap<>(scoreMap);
	}

	private void saveRanksScore(Map<Integer, PkRankerScoreData> scoreMap, String sql_delete, String sql_insert)
	{
		if(scoreMap.isEmpty())
			return;

		Connection con = null;
		PreparedStatement deleteStatement = null;
		PreparedStatement insertStatement = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();

			// Очистите таблицу перед сохранением новых данных
			deleteStatement = con.prepareStatement(sql_delete);
			deleteStatement.executeUpdate();

			// Теперь можно вставить новые данные
			insertStatement = con.prepareStatement(sql_insert);
			con.setAutoCommit(false);

			for(Map.Entry<Integer, PkRankerScoreData> entry : scoreMap.entrySet())
			{
				PkRankerScoreData data = entry.getValue();

				insertStatement.setInt(1, data.nServerRank);
				insertStatement.setInt(2, data.nCharId);
				insertStatement.setString(3, data.sUserName);
				insertStatement.setString(4, data.sPledgeName);
				insertStatement.setInt(5, data.nLevel);
				insertStatement.setInt(6, data.nClassId);
				insertStatement.setInt(7, data.nRace);
				insertStatement.setInt(8, data.nRaceRank);
				insertStatement.setInt(9, data.nClassRank);
				insertStatement.setInt(10, data.nScore);

				insertStatement.addBatch();
			}

			insertStatement.executeBatch();
			con.commit();
		}
		catch(SQLException e)
		{
			_log.error("Failed to save rank score data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(deleteStatement);
			DbUtils.closeQuietly(con, insertStatement);
		}
	}

	public void saveRanksMain(Map<Integer, PkRankerData> playerMap)
	{
		saveRanksMain(playerMap, SQL_DELETE_ALL_PLAYER_RANKS, SQL_INSERT_PLAYER_RANK);
	}

	public ConcurrentHashMap<Integer, PkRankerData> saveRanksMainSnapshot(Map<Integer, PkRankerData> playerMap)
	{
		saveRanksMain(playerMap, SQL_DELETE_ALL_PLAYER_RANKS_SNAPSHOT, SQL_INSERT_PLAYER_RANK_SNAPSHOT);
		return new ConcurrentHashMap<>(playerMap);
	}

	private void saveRanksMain(Map<Integer, PkRankerData> playerMap, String sql_delete, String sql_insert)
	{
		if(playerMap.isEmpty())
			return;
		Connection con = null;
		PreparedStatement deleteStatement = null;
		PreparedStatement insertStatement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();

			deleteStatement = con.prepareStatement(sql_delete);
			deleteStatement.executeUpdate();

			insertStatement = con.prepareStatement(sql_insert);
			con.setAutoCommit(false);
			for(Entry<Integer, PkRankerData> entry : playerMap.entrySet())
			{
				PkRankerData data = entry.getValue();

				insertStatement.setInt(1, data.nServerRank);
				insertStatement.setInt(2, data.nCharId);
				insertStatement.setString(3, data.sUserName);
				insertStatement.setInt(4, data.nLevel);
				insertStatement.setInt(5, data.nClassId);
				insertStatement.setInt(6, data.nRace);
				insertStatement.setString(7, data.sPledgeName);
				insertStatement.setInt(8, data.nRaceRank);
				insertStatement.setInt(9, data.nClassRank);
				insertStatement.addBatch();
			}

			insertStatement.executeBatch();
			con.commit();
		}
		catch(SQLException e)
		{
			_log.error("Failed to save player rank data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(deleteStatement);
			DbUtils.closeQuietly(con, insertStatement);
		}
	}

	public Map<Integer, PkRankerData> loadRanksMain()
	{
		return loadRanksMain(SQL_SELECT_ALL_PLAYER_RANKS);
	}

	public Map<Integer, PkRankerData> loadRanksMainSnapshot()
	{
		return loadRanksMain(SQL_SELECT_ALL_PLAYER_RANKS_SNAPSHOT);
	}

	private Map<Integer, PkRankerData> loadRanksMain(String sql_select)
	{
		Connection con = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		Map<Integer, PkRankerData> playerMap = new HashMap<>();

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			selectStatement = con.prepareStatement(SQL_SELECT_ALL_PLAYER_RANKS);
			resultSet = selectStatement.executeQuery();

			while(resultSet.next())
			{
				int charId = resultSet.getInt("charId");
				String name = resultSet.getString("name");
				String clanName = resultSet.getString("clanName");
				int level = resultSet.getInt("level");
				int classId = resultSet.getInt("classId");
				int race = resultSet.getInt("race");
				int classRank = resultSet.getInt("classRank");
				int raceRank = resultSet.getInt("raceRank");
				int serverRank = resultSet.getInt("rank");

				playerMap.put(serverRank, new PkRankerData(charId, name, clanName, level, classId, race, classRank, raceRank, serverRank));
			}
		}
		catch(SQLException e)
		{
			_log.error("Failed to load player rank data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, selectStatement, resultSet);
		}

		return playerMap;
	}

	public void savePetRanks(Map<Integer, StatsSet> petMap)
	{
		savePetRanks(petMap, SQL_DELETE_ALL_PET_RANKS, SQL_INSERT_PET_RANK);
	}

	public ConcurrentHashMap<Integer, StatsSet> savePetRanksSnapshot(Map<Integer, StatsSet> petMap)
	{
		savePetRanks(petMap, SQL_DELETE_ALL_PET_RANKS_SNAPSHOT, SQL_INSERT_PET_RANK_SNAPSHOT);
		return new ConcurrentHashMap<>(petMap);
	}

	public void savePetRanks(Map<Integer, StatsSet> petMap, String sql_delete, String sql_insert)
	{
		Connection con = null;
		PreparedStatement deleteStatement = null;
		PreparedStatement insertStatement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();

			// Очистите таблицу перед сохранением новых данных
			deleteStatement = con.prepareStatement(sql_delete);
			deleteStatement.executeUpdate();

			// Теперь можно вставить новые данные
			insertStatement = con.prepareStatement(sql_insert);
			con.setAutoCommit(false);

			for(Map.Entry<Integer, StatsSet> entry : petMap.entrySet())
			{
				StatsSet pet = entry.getValue();

				insertStatement.setInt(1, pet.getInteger("objId"));
				insertStatement.setInt(2, pet.getInteger("player_obj_Id"));
				insertStatement.setInt(3, pet.getInteger("item_obj_id"));
				insertStatement.setString(4, pet.getString("sUserName"));
				insertStatement.setString(5, pet.getString("sPledgeName"));
				insertStatement.setInt(6, pet.getInteger("nUserRace"));
				insertStatement.setInt(7, pet.getInteger("nUserLevel"));
				insertStatement.setInt(8, pet.getInteger("nPetIndex"));
				insertStatement.setString(9, pet.getString("sNickName"));
				insertStatement.setLong(10, pet.getLong("exp"));
				insertStatement.setInt(11, pet.getInteger("sp"));
				insertStatement.setInt(12, pet.getInteger("fed"));
				insertStatement.setInt(13, pet.getInteger("nPetLevel"));
				insertStatement.setInt(14, pet.getInteger("max_fed"));
				insertStatement.setInt(15, pet.getInteger("nNPCClassID"));
				insertStatement.setInt(16, pet.getInteger("evolve_level"));
				insertStatement.setInt(17, pet.getInteger("random_names"));
				insertStatement.setInt(18, pet.getInteger("passive_skill"));
				insertStatement.setInt(19, pet.getInteger("passive_skill_level"));
				insertStatement.setInt(20, pet.getInteger("PetIndexRank"));

				insertStatement.addBatch();
			}

			insertStatement.executeBatch();
			con.commit();
		}
		catch(SQLException e)
		{
			_log.error("Failed to save pet rank data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(deleteStatement);
			DbUtils.closeQuietly(con, insertStatement);
		}
	}

	public Map<Integer, StatsSet> loadPetRanks()
	{
		return loadPetRanks(SQL_SELECT_ALL_PET_RANKS);
	}

	public Map<Integer, StatsSet> loadPetRanksSnapshot()
	{
		return loadPetRanks(SQL_SELECT_ALL_PET_RANKS_SNAPSHOT);
	}

	private Map<Integer, StatsSet> loadPetRanks(String sql_select)
	{
		Connection con = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		Map<Integer, StatsSet> petMap = new HashMap<>();

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			selectStatement = con.prepareStatement(sql_select);
			resultSet = selectStatement.executeQuery();

			while(resultSet.next())
			{
				StatsSet pet = new StatsSet();
				pet.set("objId", resultSet.getInt("objId"));
				pet.set("player_obj_Id", resultSet.getInt("player_obj_Id"));
				pet.set("item_obj_id", resultSet.getInt("item_obj_id"));
				pet.set("sUserName", resultSet.getString("player_name"));
				pet.set("sPledgeName", resultSet.getString("clan_name"));
				pet.set("nUserRace", resultSet.getInt("user_race"));
				pet.set("nUserLevel", resultSet.getInt("user_level"));
				pet.set("nPetIndex", resultSet.getInt("pet_index"));
				pet.set("sNickName", resultSet.getString("nick_name"));
				pet.set("exp", resultSet.getLong("exp"));
				pet.set("sp", resultSet.getInt("sp"));
				pet.set("fed", resultSet.getInt("fed"));
				pet.set("nPetLevel", resultSet.getInt("pet_level"));
				pet.set("max_fed", resultSet.getInt("max_fed"));
				pet.set("nNPCClassID", resultSet.getInt("npc_class_id"));
				pet.set("evolve_level", resultSet.getInt("evolve_level"));
				pet.set("random_names", resultSet.getInt("random_names"));
				pet.set("passive_skill", resultSet.getInt("passive_skill"));
				pet.set("passive_skill_level", resultSet.getInt("passive_skill_level"));
				pet.set("PetIndexRank", resultSet.getInt("pet_index_rank"));

				int id = resultSet.getInt("id");
				petMap.put(id, pet);
			}
		}
		catch(SQLException e)
		{
			_log.error("Failed to load pet rank data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, selectStatement, resultSet);
		}

		return petMap;
	}

	public void saveOlyRanks(Map<Integer, OlympiadRankInfo> olyMap)
	{
		Connection con = null;
		PreparedStatement deleteStatement = null;
		PreparedStatement insertStatement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();

			// Очистите таблицу перед сохранением новых данных
			deleteStatement = con.prepareStatement(SQL_DELETE_ALL_OLYMPIAD_RANKS);
			deleteStatement.executeUpdate();

			// Теперь можно вставить новые данные
			insertStatement = con.prepareStatement(SQL_INSERT_OLYMPIAD_RANK);
			con.setAutoCommit(false);

			for(Entry<Integer, OlympiadRankInfo> entry : olyMap.entrySet())
			{
				OlympiadRankInfo player = entry.getValue();

				insertStatement.setInt(1, player.nCharId);
				insertStatement.setString(2, player.sCharName);
				insertStatement.setString(3, player.sPledgeName);
				insertStatement.setInt(4, player.nLevel);
				insertStatement.setInt(5, player.nClassID);
				insertStatement.setInt(6, player.nPledgeLevel);
				insertStatement.setInt(7, player.nWinCount);
				insertStatement.setInt(8, player.nLoseCount);
				insertStatement.setInt(9, player.nOlympiadPoint);

				insertStatement.addBatch();
			}

			insertStatement.executeBatch();
			con.commit();
		}
		catch(SQLException e)
		{
			_log.error("Failed to save olympiad rank data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(deleteStatement);
			DbUtils.closeQuietly(con, insertStatement);
		}
	}

	public Map<Integer, OlympiadRankInfo> loadOlyRanks()
	{
		Connection con = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		Map<Integer, OlympiadRankInfo> olyMap = new HashMap<>();

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			selectStatement = con.prepareStatement(SQL_SELECT_ALL_OLYMPIAD_RANKS);
			resultSet = selectStatement.executeQuery();

			while(resultSet.next())
			{
				int id = resultSet.getInt("id");
				OlympiadRankInfo player = new OlympiadRankInfo();
				player.nRank = id;
				player.nCharId = resultSet.getInt("charId");
				player.sCharName = resultSet.getString("name");
				player.sPledgeName = resultSet.getString("clanName");
				player.nLevel = resultSet.getInt("level");
				player.nClassID = resultSet.getInt("classId");
				player.nPledgeLevel = resultSet.getInt("clanLevel");
				player.nWinCount = resultSet.getInt("competitions_win");
				player.nLoseCount = resultSet.getInt("competitions_loose");
				player.nOlympiadPoint = resultSet.getInt("olympiad_points");
				player.nSex = resultSet.getInt("sex");
				olyMap.put(id, player);
			}

			// Calculate class ranks
			Map<Integer, Map<Integer, OlympiadRankInfo>> playersByClass = new HashMap<>();
			for(OlympiadRankInfo player : olyMap.values())
			{
				playersByClass.computeIfAbsent(player.nClassID, k -> new HashMap<>()).put(player.nCharId, player);
			}

			for(Map<Integer, OlympiadRankInfo> players : playersByClass.values())
			{
				List<OlympiadRankInfo> sortedPlayers = new ArrayList<>(players.values());
				sortedPlayers.sort((p1, p2) -> Integer.compare(p2.nOlympiadPoint, p1.nOlympiadPoint));

				int rank = 1;
				for(OlympiadRankInfo player : sortedPlayers)
				{
					player.nClassRank = rank++;
				}
			}
		}
		catch(SQLException e)
		{
			_log.error("Failed to load olympiad rank data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, selectStatement, resultSet);
		}

		return olyMap;
	}
}
