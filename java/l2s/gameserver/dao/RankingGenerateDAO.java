package l2s.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.ClassLevel;
import l2s.gameserver.model.base.PlayerAccess;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.base.SubClassType;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.ranking.OlympiadRankInfo;
import l2s.gameserver.templates.ranking.PVPRankingRankInfo;
import l2s.gameserver.templates.ranking.PkPledgeRanking;
import l2s.gameserver.templates.ranking.PkRankerData;
import l2s.gameserver.templates.ranking.PkRankerScoreData;

public class RankingGenerateDAO
{
	private static final Logger _log = LoggerFactory.getLogger(RankingGenerateDAO.class);
	private static final RankingGenerateDAO _instance = new RankingGenerateDAO();

	private static final String SELECT_CHARACTERS = "SELECT c.obj_Id,c.char_name,c.clanid,c.account_name,cs.class_id,cs.level FROM character_subclasses AS cs LEFT JOIN characters AS c ON cs.char_obj_id=c.obj_Id WHERE cs.type=? AND c.last_login > "
			+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)) + " ORDER BY cs.exp DESC";
	private static final String SELECT_PETS = "SELECT p.*, c.char_name AS player_name, c.clanid, cs.class_id, cs.level AS player_level FROM pets p LEFT JOIN characters c ON p.player_obj_Id = c.obj_Id LEFT JOIN	character_subclasses cs ON c.obj_Id = cs.char_obj_id AND cs.active = 1 WHERE p.level >= 40  AND p.evolve_level > 0 ORDER BY p.exp DESC";
	private static final String SELECT_PVP_RANKING = "SELECT c.obj_Id,c.char_name,c.clanid,cs.class_id,cs.level,prd.kills,prd.deaths,prd.points FROM character_subclasses AS cs LEFT JOIN characters AS c ON cs.char_obj_id=c.obj_Id LEFT JOIN pvp_ranking_data AS prd ON prd.obj_Id = cs.char_obj_id WHERE prd.week=? ORDER BY prd.points DESC";

	private static final String GET_CURRENT_CYCLE_DATA = "SELECT c.sex,c.obj_Id,c.char_name,c.account_name,c.clanid,cs.level,cs.class_id,op.olympiad_points,op.competitions_win,op.competitions_loose FROM olympiad_participants AS op LEFT JOIN character_subclasses AS cs ON op.char_id=cs.char_obj_id AND cs.type=? LEFT JOIN characters AS c ON op.char_id=c.obj_Id ORDER BY op.olympiad_points DESC";
	private static final String GET_PREVIOUS_OLY_DATA = "SELECT characters.sex, character_subclasses.class_id, character_subclasses.level, olympiad_participants_old.char_id, olympiad_participants_old.olympiad_points, olympiad_participants_old.competitions_win, olympiad_participants_old.competitions_loose, characters.char_name, characters.clanid FROM characters, character_subclasses, olympiad_participants_old WHERE characters.obj_Id = character_subclasses.char_obj_id AND character_subclasses.char_obj_id = olympiad_participants_old.char_id ORDER BY olympiad_points DESC";
	//private static final String GET_CHARACTERS_BY_CLASS = "SELECT cs.char_obj_id FROM character_subclasses AS cs, olympiad_participants AS op WHERE cs.char_obj_id = op.char_id AND cs.type=? AND cs.class_id=? ORDER BY op.olympiad_points DESC LIMIT " + PLAYER_LIMIT;

	private static final String GET_CHARACTERS_BY_CLASS = "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
			+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)) + " AND cs.class_id=? ORDER BY cs.exp DESC";

	private static final String SELECT_CLANS = "SELECT c.clan_id,c.clan_points FROM clan_data AS c WHERE c.disband_end = 0 ORDER by c.clan_points DESC";
	private static final String SELECT_CLANS_RANK = "SELECT c.clan_id, c.clan_points FROM clan_ranking AS c ORDER by c.clan_points DESC";
	private static final String CLEAN_CLANS_RANK = "TRUNCATE TABLE clan_ranking";
	private static final String CLEAN_CLANS_RANK2 = "ALTER TABLE clan_ranking AUTO_INCREMENT = 1;";

	private static final String SAVE_CLANS_RANK = "INSERT INTO clan_ranking (clan_id, clan_points) SELECT clan_id, clan_points FROM clan_data ORDER by clan_points DESC";

	private static final String SELECT_CHARACTERS_BY_SCORE = "SELECT c.obj_Id, c.char_name, c.clanid, cs.class_id, cs.level, c.score, c.account_name FROM character_subclasses AS cs LEFT JOIN characters AS c ON cs.char_obj_id = c.obj_Id WHERE cs.type = ? AND c.last_login > ? ORDER BY c.score DESC";
	private static final String SELECT_CHARACTERS_BY_CLASS_SCORE = "SELECT cs.char_obj_id, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND cs.class_id = ? ORDER BY c.score DESC";

	private static final String SELECT_CLANS_RB_POINTS = "SELECT clan_id, rb_id, COUNT(rb_id) AS kill_count FROM raidboss_kill_logs WHERE clan_id != 0 GROUP BY clan_id, rb_id";

	public static RankingGenerateDAO getInstance()
	{
		return _instance;
	}

	public Map<Integer, PkRankerScoreData> loadScore()
	{
		Map<Integer, PkRankerScoreData> scoreList = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		Map<Integer, Map<Integer, Integer>> raceRanks = loadRaceRanksByScore();
		Map<Integer, Map<Integer, Integer>> classRanks = loadClassRanksByScore();

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_CHARACTERS_BY_SCORE);
			statement.setInt(1, SubClassType.BASE_CLASS.ordinal());
			statement.setLong(2, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));
			rset = statement.executeQuery();
			int i = 1;
			while(rset.next())
			{
				final int level = rset.getInt("cs.level");
				if(level < 40)
				{
					continue;
				}

				String account_name = rset.getString("c.account_name");
				if(account_name.equalsIgnoreCase(Config.PHANTOM_PLAYERS_AKK))
					continue;

				final ClassId classId = ClassId.valueOf(rset.getInt("cs.class_id"));
				if(classId.getClassLevel().ordinal() < ClassLevel.SECOND.ordinal())
				{
					continue;
				}

				final int charId = rset.getInt("c.obj_Id");
				if(!Config.EVERYBODY_HAS_ADMIN_RIGHTS)
				{
					PlayerAccess playerAccess = Config.gmlist.get(charId);
					if(playerAccess != null && playerAccess.IsGM)
					{
						continue;
					}
				}

				final String name = rset.getString("c.char_name");
				final int race = classId.getRace().ordinal();
				final int clanId = rset.getInt("c.clanid");
				Clan clan = ClanTable.getInstance().getClan(clanId);
				final String clanName = (clanId > 0 && clan != null) ? clan.getName() : "";
				final int score = rset.getInt("c.score");

				int race_rank = raceRanks.getOrDefault(race, new HashMap<>()).getOrDefault(charId, 0);

				int groupKey = getClassGroupKey(classId);
				int class_rank = classRanks.getOrDefault(groupKey, new HashMap<>()).getOrDefault(charId, 0);

				scoreList.put(i, new PkRankerScoreData(charId, name, clanName, level, classId.getId(), race, class_rank, race_rank, i, score));
				i++;
			}
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not load chars total rank data by score: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return scoreList;
	}

	private Map<Integer, Map<Integer, Integer>> loadClassRanksByScore()
	{
		Map<Integer, Map<Integer, Integer>> classRanks = new HashMap<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			for(ClassId cid : ClassId.values())
			{
				int groupKey = getClassGroupKey(cid);

				Map<Integer, Integer> rankMap = classRanks.computeIfAbsent(groupKey, k -> new HashMap<>());

				ps = con.prepareStatement(SELECT_CHARACTERS_BY_CLASS_SCORE);
				ps.setLong(1, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));
				ps.setInt(2, cid.getId());
				rset = ps.executeQuery();
				int rank = 1;
				while(rset.next())
				{
					int charId = rset.getInt("cs.char_obj_id");
					rankMap.put(charId, rank);
					rank++;
				}

				DbUtils.closeQuietly(ps, rset);
			}
		}
		catch(SQLException e)
		{
			_log.error("Could not load class ranks by score: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}

		return classRanks;
	}

	private Map<Integer, Map<Integer, Integer>> loadRaceRanksByScore()
	{
		Map<Integer, Map<Integer, Integer>> raceRanks = new HashMap<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			for(Race race : Race.values())
			{
				if(race.getId() >= 6 && race.getId() <= 29)
					continue; // skip UNK races

				Map<Integer, Integer> rankMap = new HashMap<>();
				String query = getRaceQuery(race);
				if(query.isEmpty())
					continue;

				ps = con.prepareStatement(query);
				ps.setLong(1, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));
				rset = ps.executeQuery();
				int rank = 1;
				while(rset.next())
				{
					int charId = rset.getInt("cs.char_obj_id");
					rankMap.put(charId, rank);
					rank++;
				}
				raceRanks.put(race.ordinal(), rankMap);
				DbUtils.closeQuietly(ps, rset);
			}
		}
		catch(SQLException e)
		{
			_log.error("Could not load race ranks by score: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}

		return raceRanks;
	}

	private String getRaceQuery(Race race)
	{
		if(race == Race.HUMAN)
			return "SELECT cs.char_obj_id, cs.level, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND (cs.class_id = 2 OR cs.class_id = 3 OR cs.class_id = 5 OR cs.class_id = 6 OR cs.class_id = 8 OR cs.class_id = 9 OR cs.class_id = 12 OR cs.class_id = 13 OR cs.class_id = 14 OR cs.class_id = 16 OR cs.class_id = 17 OR cs.class_id = 88 OR cs.class_id = 89 OR cs.class_id = 90 OR cs.class_id = 91 OR cs.class_id = 92 OR cs.class_id = 93 OR cs.class_id = 94 OR cs.class_id = 95 OR cs.class_id = 96 OR cs.class_id = 97 OR cs.class_id = 98 OR cs.class_id = 198 OR cs.class_id = 199 OR cs.class_id = 223 OR cs.class_id = 224 OR cs.class_id = 249 OR cs.class_id = 250) ORDER BY c.score DESC";
		else if(race == Race.ELF)
			return "SELECT cs.char_obj_id, cs.level, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND (cs.class_id = 20 OR cs.class_id = 21 OR cs.class_id = 23 OR cs.class_id = 24 OR cs.class_id = 27 OR cs.class_id = 28 OR cs.class_id = 30 OR cs.class_id = 99 OR cs.class_id = 100 OR cs.class_id = 101 OR cs.class_id = 102 OR cs.class_id = 103 OR cs.class_id = 104 OR cs.class_id = 105 OR cs.class_id = 202 OR cs.class_id = 203) ORDER BY c.score DESC";
		else if(race == Race.DARKELF)
			return "SELECT cs.char_obj_id, cs.level, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND (cs.class_id = 33 OR cs.class_id = 34 OR cs.class_id = 36 OR cs.class_id = 37 OR cs.class_id = 40 OR cs.class_id = 41 OR cs.class_id = 43 OR cs.class_id = 106 OR cs.class_id = 107 OR cs.class_id = 108 OR cs.class_id = 109 OR cs.class_id = 110 OR cs.class_id = 111 OR cs.class_id = 112 OR cs.class_id = 206 OR cs.class_id = 207 OR cs.class_id = 227 OR cs.class_id = 228) ORDER BY c.score DESC";
		else if(race == Race.ORC)
			return "SELECT cs.char_obj_id, cs.level, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND (cs.class_id = 46 OR cs.class_id = 48 OR cs.class_id = 51 OR cs.class_id = 52 OR cs.class_id = 113 OR cs.class_id = 114 OR cs.class_id = 115 OR cs.class_id = 116) ORDER BY c.score DESC";
		else if(race == Race.DWARF)
			return "SELECT cs.char_obj_id, cs.level, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND (cs.class_id = 55 OR cs.class_id = 57 OR cs.class_id = 117 OR cs.class_id = 118) ORDER BY c.score DESC";
		else if(race == Race.KAMAEL)
			return "SELECT cs.char_obj_id, cs.level, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND (cs.class_id = 127 OR cs.class_id = 130 OR cs.class_id = 131 OR cs.class_id = 134 OR cs.class_id = 194 OR cs.class_id = 195) ORDER BY c.score DESC";
		else if(race == Race.SYLPH)
			return "SELECT cs.char_obj_id, cs.level, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND (cs.class_id = 210 OR cs.class_id = 211) ORDER BY c.score DESC";
		else if(race == Race.highelf)
			return "SELECT cs.char_obj_id, cs.level, c.score FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > ? AND (cs.class_id = 239 OR cs.class_id = 243) ORDER BY c.score DESC";
		else
			return "";
	}

	public Map<Integer, PkRankerData> loadMain()
	{
		Map<Integer, PkRankerData> _mainList = new HashMap<>();

		Map<Integer, Map<Integer, Integer>> raceRanks = loadRaceRanks();
		Map<Integer, Map<Integer, Integer>> classRanks = loadClassRanks();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_CHARACTERS);
			statement.setInt(1, SubClassType.BASE_CLASS.ordinal());
			rset = statement.executeQuery();
			int i = 1;

			while(rset.next())
			{
				final int level = rset.getInt("cs.level");
				if(level < 40)
					continue;

				final ClassId classId = ClassId.valueOf(rset.getInt("cs.class_id"));
				if(classId.getClassLevel().ordinal() < ClassLevel.SECOND.ordinal())
					continue;

				String account_name = rset.getString("c.account_name");
				if(account_name.equalsIgnoreCase(Config.PHANTOM_PLAYERS_AKK))
					continue;
				final int charId = rset.getInt("c.obj_Id");
				if(!Config.EVERYBODY_HAS_ADMIN_RIGHTS)
				{
					PlayerAccess playerAccess = Config.gmlist.get(charId);
					if(playerAccess != null && playerAccess.IsGM)
						continue;
				}

				final String name = rset.getString("c.char_name");
				final int race = classId.getRace().ordinal();
				final int clanId = rset.getInt("c.clanid");
				Clan clan = ClanTable.getInstance().getClan(clanId);
				final String clanName = (clanId > 0 && clan != null) ? clan.getName() : "";

				int race_rank = raceRanks.getOrDefault(race, new HashMap<>()).getOrDefault(charId, 0);
				int groupKey = getClassGroupKey(classId);
				int class_rank = classRanks.getOrDefault(groupKey, new HashMap<>()).getOrDefault(charId, 0);

				_mainList.put(i, new PkRankerData(charId, name, clanName, level, classId.getId(), race, class_rank, race_rank, i));
				i++;
			}
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not load chars total rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return _mainList;
	}

	private Map<Integer, Map<Integer, Integer>> loadRaceRanks()
	{
		Map<Integer, Map<Integer, Integer>> raceRanks = new HashMap<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			for(Race race : Race.values())
			{
				if(race.ordinal() >= 6 && race.ordinal() <= 29)
					continue; // skip UNK races

				Map<Integer, Integer> rankMap = new HashMap<>();
				String query = getRaceQueryByExp(race);
				if(query.isEmpty())
					continue;

				ps = con.prepareStatement(query);
				rset = ps.executeQuery();
				int rank = 1;
				while(rset.next())
				{
					int charId = rset.getInt("cs.char_obj_id");
					rankMap.put(charId, rank);
					rank++;
				}
				raceRanks.put(race.ordinal(), rankMap);
				DbUtils.closeQuietly(ps, rset);
			}
		}
		catch(SQLException e)
		{
			_log.error("Could not load race ranks by exp: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con);
		}

		return raceRanks;
	}

	private String getRaceQueryByExp(Race race)
	{
		if(race == Race.HUMAN)
			return "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
					+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
					+ " AND (cs.class_id = 2 OR cs.class_id = 3 OR cs.class_id = 5 OR cs.class_id = 6 OR cs.class_id = 8 OR cs.class_id = 9 OR cs.class_id = 12 OR cs.class_id = 13 OR cs.class_id = 14 OR cs.class_id = 16 OR cs.class_id = 17 OR cs.class_id = 88 OR cs.class_id = 89 OR cs.class_id = 90 OR cs.class_id = 91 OR cs.class_id = 92 OR cs.class_id = 93 OR cs.class_id = 94 OR cs.class_id = 95 OR cs.class_id = 96 OR cs.class_id = 97 OR cs.class_id = 98 OR cs.class_id = 198 OR cs.class_id = 199 OR cs.class_id = 223 OR cs.class_id = 224) ORDER BY cs.exp DESC";
		else if(race == Race.ELF)
			return "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
					+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
					+ " AND (cs.class_id = 20 OR cs.class_id = 21 OR cs.class_id = 23 OR cs.class_id = 24 OR cs.class_id = 27 OR cs.class_id = 28 OR cs.class_id = 30 OR cs.class_id = 99 OR cs.class_id = 100 OR cs.class_id = 101 OR cs.class_id = 102 OR cs.class_id = 103 OR cs.class_id = 104 OR cs.class_id = 105 OR cs.class_id = 202 OR cs.class_id = 203) ORDER BY cs.exp DESC";
		else if(race == Race.DARKELF)
			return "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
					+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
					+ " AND (cs.class_id = 33 OR cs.class_id = 34 OR cs.class_id = 36 OR cs.class_id = 37 OR cs.class_id = 40 OR cs.class_id = 41 OR cs.class_id = 43 OR cs.class_id = 106 OR cs.class_id = 107 OR cs.class_id = 108 OR cs.class_id = 109 OR cs.class_id = 110 OR cs.class_id = 111 OR cs.class_id = 112 OR cs.class_id = 206 OR cs.class_id = 207 OR cs.class_id = 227 OR cs.class_id = 228) ORDER BY cs.exp DESC";
		else if(race == Race.ORC)
			return "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
					+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
					+ " AND (cs.class_id = 46 OR cs.class_id = 48 OR cs.class_id = 51 OR cs.class_id = 52 OR cs.class_id = 113 OR cs.class_id = 114 OR cs.class_id = 115 OR cs.class_id = 116) ORDER BY cs.exp DESC";
		else if(race == Race.DWARF)
			return "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
					+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
					+ " AND (cs.class_id = 55 OR cs.class_id = 57 OR cs.class_id = 117 OR cs.class_id = 118) ORDER BY cs.exp DESC";
		else if(race == Race.KAMAEL)
			return "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
					+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
					+ " AND (cs.class_id = 127 OR cs.class_id = 130 OR cs.class_id = 131 OR cs.class_id = 134 OR cs.class_id = 194 OR cs.class_id = 195) ORDER BY cs.exp DESC";
		else if(race == Race.SYLPH)
			return "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
					+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)) + " AND (cs.class_id = 210 OR cs.class_id = 211) ORDER BY cs.exp DESC";
		else if(race == Race.highelf)
			return "SELECT cs.char_obj_id, cs.level FROM character_subclasses AS cs, characters AS c WHERE cs.char_obj_id = c.obj_Id AND c.last_login > "
					+ (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)) + " AND (cs.class_id = 239 OR cs.class_id = 243) ORDER BY cs.exp DESC";
		else
			return "";
	}

	public Map<Integer, OlympiadRankInfo> loadOlyMain()
	{
		Map<Integer, OlympiadRankInfo> mainOlyList = new HashMap<>();
		Map<Integer, Map<Integer, Integer>> classRanks = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(GET_CURRENT_CYCLE_DATA);
			statement.setInt(1, SubClassType.BASE_CLASS.ordinal());
			rset = statement.executeQuery();
			int i = 1;
			while(rset.next())
			{
				String account_name = rset.getString("c.account_name");
				if(account_name.equalsIgnoreCase(Config.PHANTOM_PLAYERS_AKK))
					continue;

				OlympiadRankInfo player = new OlympiadRankInfo();
				int charId = rset.getInt("c.obj_Id");
				player.nCharId = charId;
				player.sCharName = rset.getString("c.char_name");
				int clanId = rset.getInt("c.clanid");
				Clan clan = ClanTable.getInstance().getClan(clanId);
				final String clanName = (clanId > 0 && clan != null) ? clan.getName() : "";
				player.sPledgeName = clanName;
				player.nLevel = rset.getInt("cs.level");
				player.nClassID = rset.getInt("cs.class_id");
				player.nPledgeLevel = clanId > 0 ? ClanTable.getInstance().getClan(clanId).getLevel() : 0;
				player.nWinCount = rset.getInt("op.competitions_win");
				player.nLoseCount = rset.getInt("op.competitions_loose");
				player.nOlympiadPoint = rset.getInt("op.olympiad_points");
				player.nSex = rset.getInt("c.sex");

				StatsSet hero = Hero.getInstance().getHeroes().get(charId);
				if(hero != null)
				{
					player.nHeroCount = hero.getInteger(Hero.COUNT, 0);
					player.nLegendCount = 0; // TODO: Implement legend count logic
				}
				else
				{
					player.nHeroCount = 0;
					player.nLegendCount = 0;
				}

				mainOlyList.put(i, player);
				i++;
			}

			// Calculate class ranks from the loaded players
			calculateClassRanks(mainOlyList, classRanks);

			// Assign calculated class ranks back to the players
			for(Map.Entry<Integer, OlympiadRankInfo> entry : mainOlyList.entrySet())
			{
				OlympiadRankInfo player = entry.getValue();
				player.nClassRank = classRanks.getOrDefault(player.nClassID, new HashMap<>()).getOrDefault(player.nCharId, 0);
			}
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not load olympiad total rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return mainOlyList;
	}

	private void calculateClassRanks(Map<Integer, OlympiadRankInfo> playerList, Map<Integer, Map<Integer, Integer>> classRanks)
	{
		for(OlympiadRankInfo player : playerList.values())
		{
			int classId = player.nClassID;
			Map<Integer, Integer> rankMap = classRanks.computeIfAbsent(classId, k -> new HashMap<>());
			rankMap.put(player.nCharId, rankMap.size() + 1);
		}
	}

	public Map<Integer, OlympiadRankInfo> loadPreviousOlyMain()
	{
		Map<Integer, OlympiadRankInfo> previousOlyList = new HashMap<>();
		Map<Integer, Map<Integer, Integer>> classRanks = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(GET_PREVIOUS_OLY_DATA);
			rset = statement.executeQuery();
			int i = 1;
			while(rset.next())
			{
				OlympiadRankInfo player = new OlympiadRankInfo();
				int charId = rset.getInt("olympiad_participants_old.char_id");
				player.nCharId = charId;
				player.sCharName = rset.getString("characters.char_name");
				int clanId = rset.getInt("characters.clanid");
				player.sPledgeName = clanId > 0 ? ClanTable.getInstance().getClan(clanId).getName() : "";
				player.nPledgeLevel = clanId > 0 ? ClanTable.getInstance().getClan(clanId).getLevel() : 0;
				player.nClassID = rset.getInt("character_subclasses.class_id");
				player.nLevel = rset.getInt("character_subclasses.level");
				player.nOlympiadPoint = rset.getInt("olympiad_participants_old.olympiad_points");
				player.nWinCount = rset.getInt("olympiad_participants_old.competitions_win");
				player.nLoseCount = rset.getInt("olympiad_participants_old.competitions_loose");
				player.nSex = rset.getInt("characters.sex");

				StatsSet hero = Hero.getInstance().getHeroes().get(charId);
				if(hero != null)
				{
					player.nHeroCount = hero.getInteger(Hero.COUNT, 0);
					player.nLegendCount = 0; // TODO: Implement legend count logic
				}
				else
				{
					player.nHeroCount = 0;
					player.nLegendCount = 0;
				}

				previousOlyList.put(i, player);
				i++;
			}

			// Calculate class ranks from the loaded players
			calculateClassRanks(previousOlyList, classRanks);

			// Assign calculated class ranks back to the players
			for(Map.Entry<Integer, OlympiadRankInfo> entry : previousOlyList.entrySet())
			{
				OlympiadRankInfo player = entry.getValue();
				player.nClassRank = classRanks.getOrDefault(player.nClassID, new HashMap<>()).getOrDefault(player.nCharId, 0);
			}
		}
		catch(Exception e)
		{
			_log.error("RankManager: Could not load previous month olympiad data: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return previousOlyList;
	}

	// load pvp ranking data this week
	public Map<Integer, PVPRankingRankInfo> loadPvPMain()
	{
		Map<Integer, PVPRankingRankInfo> _mainPvpList = new HashMap<>();

		Map<Integer, Map<Integer, Integer>> raceRanks = loadRaceRanks();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_PVP_RANKING);
			statement.setInt(1, 0);
			rset = statement.executeQuery();
			int i = 1;
			while(rset.next())
			{
				final int level = rset.getInt("cs.level");
				if(level < 40)
					continue;

				final ClassId classId = ClassId.valueOf(rset.getInt("cs.class_id"));
				if(classId.getClassLevel().ordinal() < ClassLevel.SECOND.ordinal())
					continue;

				final int charId = rset.getInt("c.obj_Id");
				if(!Config.EVERYBODY_HAS_ADMIN_RIGHTS)
				{
					PlayerAccess playerAccess = Config.gmlist.get(charId);
					if(playerAccess != null && playerAccess.IsGM)
						continue;
				}

				final String charName = rset.getString("c.char_name");
				final int clanId = rset.getInt("c.clanid");
				Clan clan = ClanTable.getInstance().getClan(clanId);
				final String clanName = (clanId > 0 && clan != null) ? clan.getName() : "";
				final int race = classId.getRace().ordinal();
				final int kills = rset.getInt("prd.kills");
				final int deaths = rset.getInt("prd.deaths");
				final long points = rset.getLong("prd.points");

				int race_rank = raceRanks.getOrDefault(race, new HashMap<>()).getOrDefault(charId, 0);

				_mainPvpList.put(i, new PVPRankingRankInfo(charId, charName, clanName, level, race, classId.getId(), points, i, race_rank, kills, deaths));
				i++;
			}
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not load pvp rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return _mainPvpList;
	}

	public Map<Integer, PVPRankingRankInfo> loadPvPOld()
	{
		Map<Integer, PVPRankingRankInfo> _oldPvpList = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		Map<Integer, Map<Integer, Integer>> raceRanks = loadRaceRanks();

		// load pvp ranking data previous week
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_PVP_RANKING);
			statement.setInt(1, 1);
			rset = statement.executeQuery();
			int i = 1;
			while(rset.next())
			{
				final int level = rset.getInt("cs.level");
				if(level < 40)
					continue;

				final ClassId classId = ClassId.valueOf(rset.getInt("cs.class_id"));
				if(classId.getClassLevel().ordinal() < ClassLevel.SECOND.ordinal())
					continue;

				final int charId = rset.getInt("c.obj_Id");
				if(!Config.EVERYBODY_HAS_ADMIN_RIGHTS)
				{
					PlayerAccess playerAccess = Config.gmlist.get(charId);
					if(playerAccess != null && playerAccess.IsGM)
						continue;
				}
				final String charName = rset.getString("c.char_name");
				final int clanId = rset.getInt("c.clanid");
				Clan clan = ClanTable.getInstance().getClan(clanId);
				final String clanName = (clanId > 0 && clan != null) ? clan.getName() : "";
				final int race = classId.getRace().ordinal();
				final int kills = rset.getInt("prd.kills");
				final int deaths = rset.getInt("prd.deaths");
				final long points = rset.getLong("prd.points");

				int race_rank = raceRanks.getOrDefault(race, new HashMap<>()).getOrDefault(charId, 0);

				_oldPvpList.put(i, new PVPRankingRankInfo(charId, charName, clanName, level, race, classId.getId(), points, i, race_rank, kills, deaths));
				i++;
			}
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not load pvp rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return _oldPvpList;
	}

	private Map<Integer, Map<Integer, Integer>> loadClassRanks()
	{
		Map<Integer, Map<Integer, Integer>> classRanks = new HashMap<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			for(ClassId cid : ClassId.values())
			{
				int groupKey = getClassGroupKey(cid);

				Map<Integer, Integer> rankMap = classRanks.computeIfAbsent(groupKey, k -> new HashMap<>());

				ps = con.prepareStatement(GET_CHARACTERS_BY_CLASS);
				ps.setInt(1, cid.getId());
				rset = ps.executeQuery();
				int rank = 1;
				while(rset.next())
				{
					int charId = rset.getInt("cs.char_obj_id");
					rankMap.put(charId, rank);
					rank++;
				}
				DbUtils.closeQuietly(ps, rset);
			}
		}
		catch(SQLException e)
		{
			_log.error("Could not load class ranks by exp: " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, ps);
		}

		return classRanks;
	}

	public Map<Integer, PkPledgeRanking> loadClansRbPointsRank()
	{
		Map<Integer, PkPledgeRanking> clanList = new HashMap<>();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_CLANS_RB_POINTS);
			rset = statement.executeQuery();

			// Ключ - Clan ID, Значение - общее количество очков за RB
			Map<Integer, Integer> clanRbPoints = new HashMap<>();

			while(rset.next())
			{
				int clanId = rset.getInt("clan_id");
				int rbId = rset.getInt("rb_id");
				int killCount = rset.getInt("kill_count");

				int pointsForRb = Config.CLAN_POINTS_FOR_KILL_RB.getOrDefault(rbId, 0);
				int totalPoints = pointsForRb * killCount;

				clanRbPoints.merge(clanId, totalPoints, Integer::sum);
			}

			// Создаём рейтинг кланов
			int rank = 1;
			for(Map.Entry<Integer, Integer> entry : clanRbPoints.entrySet().stream().sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue())).toList())
			{
				int clanId = entry.getKey();
				int totalPoints = entry.getValue();

				Clan clan = ClanTable.getInstance().getClan(clanId);
				if(clan == null)
				{
					_log.warn("RankManager: not found Clan oid: " + clanId);
					continue;
				}

				PkPledgeRanking ranking = new PkPledgeRanking();
				ranking.nRank = rank++;
				ranking.sPledgeName = clan.getName();
				ranking.nPledgeLevel = clan.getLevel();
				ranking.sPledgeMasterName = clan.getLeaderName();
				ranking.nPledgeMasterLevel = clan.getLeader().getLevel();
				ranking.nPledgeMemberCount = clan.getAllMembers().size();
				ranking.nPledgeExp = totalPoints;

				clanList.put(clanId, ranking);
			}
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not load clan RB rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		// Сортируем по nRank
		List<Map.Entry<Integer, PkPledgeRanking>> sortedList = new ArrayList<>(clanList.entrySet());
		sortedList.sort((entry1, entry2) -> Integer.compare(entry1.getValue().nRank, entry2.getValue().nRank));

		// Перемещаем отсортированные элементы в новый LinkedHashMap для сохранения порядка
		Map<Integer, PkPledgeRanking> sortedClanList = new LinkedHashMap<>();
		for(Map.Entry<Integer, PkPledgeRanking> entry : sortedList)
		{
			sortedClanList.put(entry.getKey(), entry.getValue());
		}

		return sortedClanList;
	}

	public Map<Integer, PkPledgeRanking> loadClansRank()
	{
		Map<Integer, PkPledgeRanking> clanList = new HashMap<>();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_CLANS);
			rset = statement.executeQuery();
			int rank = 1;
			while(rset.next())
			{
				int clanId = rset.getInt("clan_id");
				int points = rset.getInt("clan_points");

				Clan clan = ClanTable.getInstance().getClan(clanId);
				if(clan == null)
				{
					_log.warn("RankManager: not found Clan oid: " + clanId);
					continue;
				}
				PkPledgeRanking ranking = new PkPledgeRanking();
				ranking.nRank = rank++;
				ranking.sPledgeName = clan.getName();
				ranking.nPledgeLevel = clan.getLevel();
				ranking.sPledgeMasterName = clan.getLeaderName();
				ranking.nPledgeMasterLevel = clan.getLeader().getLevel();
				ranking.nPledgeMemberCount = clan.getAllMembers().size();
				ranking.nPledgeExp = points;

				clanList.put(clanId, ranking);
			}
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not load clan rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		// Сортируем по nRank
		List<Map.Entry<Integer, PkPledgeRanking>> sortedList = new ArrayList<>(clanList.entrySet());
		sortedList.sort((entry1, entry2) -> Integer.compare(entry1.getValue().nRank, entry2.getValue().nRank));

		// Перемещаем отсортированные элементы в новый LinkedHashMap для сохранения порядка
		Map<Integer, PkPledgeRanking> sortedClanList = new LinkedHashMap<>();
		for(Map.Entry<Integer, PkPledgeRanking> entry : sortedList)
		{
			sortedClanList.put(entry.getKey(), entry.getValue());
		}

		return sortedClanList;
	}

	public Map<Integer, PkPledgeRanking> loadPreviousClanData()
	{
		Map<Integer, PkPledgeRanking> previousClanList = new HashMap<>();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_CLANS_RANK);
			rset = statement.executeQuery();
			int rank = 1;
			while(rset.next())
			{
				int clanId = rset.getInt("clan_id");
				int points = rset.getInt("clan_points");

				Clan clan = ClanTable.getInstance().getClan(clanId);
				if(clan == null)
				{
					_log.warn("RankManager: not found Clan oid: " + clanId);
					continue;
				}

				PkPledgeRanking ranking = new PkPledgeRanking();
				ranking.nPrevRank = rank++;
				ranking.sPledgeName = clan.getName();
				ranking.nPledgeLevel = clan.getLevel();
				ranking.sPledgeMasterName = clan.getLeaderName();
				ranking.nPledgeMasterLevel = clan.getLeader().getLevel();
				ranking.nPledgeMemberCount = clan.getAllMembers().size();
				ranking.nPledgeExp = points;

				previousClanList.put(clanId, ranking);
			}
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not load previous clan rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		// Сортируем по nRank
		List<Map.Entry<Integer, PkPledgeRanking>> sortedList = new ArrayList<>(previousClanList.entrySet());
		sortedList.sort((entry1, entry2) -> Integer.compare(entry1.getValue().nRank, entry2.getValue().nRank));

		// Перемещаем отсортированные элементы в новый LinkedHashMap для сохранения порядка
		Map<Integer, PkPledgeRanking> sortedClanList = new LinkedHashMap<>();
		for(Map.Entry<Integer, PkPledgeRanking> entry : sortedList)
		{
			sortedClanList.put(entry.getKey(), entry.getValue());
		}

		return sortedClanList;
	}

	public void saveClanData()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CLEAN_CLANS_RANK);
			statement.execute();
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not delete previous clan rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CLEAN_CLANS_RANK2);
			statement.execute();
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not delete previous clan rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SAVE_CLANS_RANK);
			statement.execute();
		}
		catch(SQLException e)
		{
			_log.error("RankManager: Could not save clan rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public Map<Integer, StatsSet> loadPets()
	{
		Map<Integer, StatsSet> _petList = new HashMap<>();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_PETS);
			rset = statement.executeQuery();
			int i = 1;
			while(rset.next())
			{
				final StatsSet pet = new StatsSet();

				pet.set("sUserName", rset.getString("player_name"));
				pet.set("sPledgeName", rset.getInt("clanid") > 0 ? ClanTable.getInstance().getClan(rset.getInt("clanid")).getName() : "");

				int objId = rset.getInt("objId");
				int pet_index = rset.getInt("pet_index");

				pet.set("item_obj_id", rset.getInt("item_obj_id"));
				pet.set("player_obj_Id", rset.getInt("player_obj_Id"));

				final ClassId classId = ClassId.valueOf(rset.getInt("cs.class_id"));
				final int race = classId.getRace().ordinal();

				pet.set("nUserRace", race);
				pet.set("nUserLevel", rset.getInt("player_level"));
				pet.set("objId", objId);
				pet.set("sNickName", rset.getString("name"));
				pet.set("exp", rset.getLong("exp"));
				pet.set("sp", rset.getInt("sp"));
				pet.set("fed", rset.getInt("fed"));
				pet.set("nPetLevel", rset.getInt("level"));
				pet.set("max_fed", rset.getInt("max_fed"));
				pet.set("nNPCClassID", rset.getInt("npcid"));
				pet.set("evolve_level", rset.getInt("evolve_level"));
				pet.set("random_names", rset.getInt("random_names"));
				pet.set("passive_skill", rset.getInt("passive_skill"));
				pet.set("passive_skill_level", rset.getInt("passive_skill_level"));
				pet.set("nPetIndex", pet_index);

				loadPetRankByIndex(objId, pet_index, pet);

				_petList.put(i, pet);
				i++;
			}
		}
		catch(SQLException e)
		{

		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return _petList;
	}

	private void loadPetRankByIndex(int petId, int index, StatsSet pet)
	{
		String SELECT_PETS_BY_INDEX = "SELECT * FROM pets WHERE level >= 40 AND evolve_level > 0 AND pet_index = " + index + " ORDER BY exp DESC";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			ps = con.prepareStatement(SELECT_PETS_BY_INDEX);
			rset = ps.executeQuery();
			int i = 0;
			while(rset.next())
			{
				final int objId = rset.getInt("objId");

				i++;

				if(objId == petId)
				{
					pet.set("PetIndexRank", i);
					break;
				}
			}
			if(i == 0)
			{
				pet.set("PetIndexRank", 0);
			}
		}
		catch(SQLException e)
		{
			_log.error("Could not load chars race rank data: " + this + " - " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, ps, rset);
		}
	}

	/** Единый ключ группы 2-й+3-й профессии */
	public static final int getClassGroupKey(ClassId cid)
	{
		return (cid.getClassLevel() == ClassLevel.THIRD && cid.getParent() != null) ? cid.getParent().getId() : cid.getId();
	}
}
