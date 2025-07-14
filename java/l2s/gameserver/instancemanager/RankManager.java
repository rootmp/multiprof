package l2s.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbutils.DbUtils;
import l2s.gameserver.GameServer;
import l2s.gameserver.dao.RankingDAO;
import l2s.gameserver.dao.RankingGenerateDAO;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.listener.game.OnShutdownListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.ClassLevel;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.ranking.OlympiadRankInfo;
import l2s.gameserver.templates.ranking.PVPRankingRankInfo;
import l2s.gameserver.templates.ranking.PkPledgeRanking;
import l2s.gameserver.templates.ranking.PkRankerData;
import l2s.gameserver.templates.ranking.PkRankerScoreData;

public class RankManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RankManager.class);

	private static final SkillEntry SERVER_SCORE_RANKING_1ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200017, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_2ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200018, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_3ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200019, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_4ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200020, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_5ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200021, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_6ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200022, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_7ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200023, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_8ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200024, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_9ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200025, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_10ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200026, 1);
	private static final SkillEntry SERVER_SCORE_RANKING_11_100ST = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200027, 1);

	private static final SkillEntry SERVER_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60003, 1);
	private static final SkillEntry SERVER_LEVEL_RANKING_2ND_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60004, 1);
	private static final SkillEntry SERVER_LEVEL_RANKING_3RD_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60005, 1);

	private static final SkillEntry SERVER_RANKING_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 200003, 1);

	private static final SkillEntry HUMAN_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60006, 1);
	private static final SkillEntry ELF_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60007, 1);
	private static final SkillEntry DARK_ELF_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60008, 1);
	private static final SkillEntry ORC_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60009, 1);
	private static final SkillEntry DWARF_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60010, 1);
	private static final SkillEntry KAMAEL_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60011, 1);
	private static final SkillEntry SYLPH_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 46033, 1);
	private static final SkillEntry HIGHELF_LEVEL_RANKING_1ST_CLASS = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 46034, 1);

	private static final SkillEntry HUMAN_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54204, 1);
	private static final SkillEntry ELF_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54210, 1);
	private static final SkillEntry DARK_ELF_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54211, 1);
	private static final SkillEntry ORC_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54209, 1);
	private static final SkillEntry DWARF_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54212, 1);
	private static final SkillEntry KAMAEL_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54205, 1);
	private static final SkillEntry DEATH_KNIGHT_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54208, 1);
	private static final SkillEntry SYLPH_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54226, 1);
	private static final SkillEntry HIGHELF_RANKING_BENEFIT = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 54254, 1);

	private static final String SELECT_SUBJUGATION_RANKING = "SELECT character_subjugation.*, characters.char_name, character_subjugation.`points` + character_subjugation.`keys` * 1000000 as `total` FROM `character_subjugation` JOIN `characters` ON characters.`obj_Id`=character_subjugation.`charId` WHERE character_subjugation.`category`=? ORDER BY `total` DESC";

	private Map<Integer, PkRankerData> _mainList = new ConcurrentHashMap<>();
	private Map<Integer, PkRankerData> _snapshotMainList = new ConcurrentHashMap<>();

	private Map<Integer, OlympiadRankInfo> _mainOlyList = new ConcurrentHashMap<>();
	private Map<Integer, OlympiadRankInfo> _previousOlyList = new ConcurrentHashMap<>();

	private Map<Integer, PVPRankingRankInfo> _mainPvpList = new ConcurrentHashMap<>();
	private Map<Integer, PVPRankingRankInfo> _snapshotPvpList = new ConcurrentHashMap<>();

	private Map<Integer, StatsSet> _petList = new ConcurrentHashMap<>();
	private Map<Integer, StatsSet> _snapshotPetList = new ConcurrentHashMap<>();

	private Map<Integer, PkPledgeRanking> _clanList = new ConcurrentHashMap<>();
	private Map<Integer, PkPledgeRanking> _snapshotClanList = new ConcurrentHashMap<>();

	private Map<Integer, PkPledgeRanking> _clanRbPointsList = new ConcurrentHashMap<>();
	private Map<Integer, PkPledgeRanking> _snapshotClanRbPointsList = new ConcurrentHashMap<>();

	@SuppressWarnings("unused")
	private Map<Integer, Integer> _mainInstanceZoneList = new ConcurrentHashMap<>();
	@SuppressWarnings("unused")
	private Map<Integer, Integer> _snapshotInstanceZoneList = new ConcurrentHashMap<>();

	private Map<Integer, PkRankerScoreData> _scoreList = new ConcurrentHashMap<>();
	private Map<Integer, PkRankerScoreData> _snapshotScoreList = new ConcurrentHashMap<>();

	private class OnShutdownListenerImpl implements OnShutdownListener
	{
		@Override
		public void onShutdown()
		{
			RankManager.getInstance().save();
		}
	}

	protected RankManager()
	{
		GameServer.getInstance().addListener(new OnShutdownListenerImpl());
		load();
		if(ServerVariables.getLong("lastClanUpdate", 0) <= (System.currentTimeMillis() - 86400000))
		{
			RankingGenerateDAO.getInstance().saveClanData();
			_snapshotClanList = RankingGenerateDAO.getInstance().loadPreviousClanData();

			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
			String dt = formatter.format(date) + " 06:30:00";
			formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			try
			{
				date = formatter.parse(dt);
				ServerVariables.set("lastClanUpdate", date.getTime());
			}
			catch(ParseException e)
			{
				e.printStackTrace();
			}
		}
		else
			_snapshotClanList = RankingGenerateDAO.getInstance().loadPreviousClanData();
	}

	private void load()
	{
		_snapshotMainList = RankingDAO.getInstance().loadRanksMainSnapshot();
		_snapshotPetList = RankingDAO.getInstance().loadPetRanksSnapshot();
		_mainOlyList = RankingDAO.getInstance().loadOlyRanks();
		_previousOlyList = RankingGenerateDAO.getInstance().loadPreviousOlyMain();

		_snapshotPvpList = RankingGenerateDAO.getInstance().loadPvPOld();

		_mainList = RankingDAO.getInstance().loadRanksMain();
		_petList = RankingDAO.getInstance().loadPetRanks();
		_mainPvpList = RankingGenerateDAO.getInstance().loadPvPMain();
		_clanList = RankingGenerateDAO.getInstance().loadClansRank();
		_clanRbPointsList = RankingGenerateDAO.getInstance().loadClansRbPointsRank();

		_scoreList = RankingDAO.getInstance().loadRanksScore();
		_snapshotScoreList = RankingDAO.getInstance().loadRanksScoreSnapshot();

		updateRankEffects();
	}

	public void save()
	{
		RankingDAO.getInstance().saveRanksMain(_mainList);
		RankingDAO.getInstance().saveRanksScore(_scoreList);
		RankingDAO.getInstance().savePetRanks(_petList);
		RankingDAO.getInstance().saveOlyRanks(_mainOlyList);
	}

	public void update()
	{
		_mainList = RankingGenerateDAO.getInstance().loadMain();
		_scoreList = RankingGenerateDAO.getInstance().loadScore();

		_petList = RankingGenerateDAO.getInstance().loadPets();
		_mainOlyList = RankingGenerateDAO.getInstance().loadOlyMain();

		_mainPvpList = RankingGenerateDAO.getInstance().loadPvPMain();
		_clanList = RankingGenerateDAO.getInstance().loadClansRank();

		_snapshotClanRbPointsList = _clanRbPointsList;
		_clanRbPointsList = RankingGenerateDAO.getInstance().loadClansRbPointsRank();
	}

	public void updateMonday()
	{
		_snapshotPvpList = RankingGenerateDAO.getInstance().loadPvPOld();
		_mainPvpList = RankingGenerateDAO.getInstance().loadPvPMain();
	}

	public void updateDaily()
	{
		//сохраняем
		_snapshotMainList = RankingDAO.getInstance().saveRanksMainSnapshot(_mainList);
		_snapshotScoreList = RankingDAO.getInstance().saveRanksScoreSnapshot(_scoreList);
		_snapshotPetList = RankingDAO.getInstance().savePetRanksSnapshot(_petList);

		_clanList = RankingGenerateDAO.getInstance().loadClansRank();
		_clanRbPointsList = RankingGenerateDAO.getInstance().loadClansRbPointsRank();

		//формируем новою стату
		_mainList = RankingGenerateDAO.getInstance().loadMain();
		RankingDAO.getInstance().saveRanksMain(_mainList);

		_scoreList = RankingGenerateDAO.getInstance().loadScore();
		RankingDAO.getInstance().saveRanksScore(_scoreList);

		_petList = RankingGenerateDAO.getInstance().loadPets();
		RankingDAO.getInstance().savePetRanks(_petList);

		_mainOlyList = RankingGenerateDAO.getInstance().loadOlyMain();
		RankingDAO.getInstance().saveOlyRanks(_mainOlyList);

		updateRankEffects();
	}

	public Map<Integer, PkRankerData> getRankList()
	{
		return _mainList;
	}

	public Map<Integer, PkRankerData> getSnapshotList()
	{
		return _snapshotMainList;
	}

	public Map<Integer, OlympiadRankInfo> getOlyRankList()
	{
		return _mainOlyList;
	}

	public Map<Integer, OlympiadRankInfo> getPreviousOlyList()
	{
		return _previousOlyList;
	}

	public Map<Integer, PVPRankingRankInfo> getPvpRankList()
	{
		return _mainPvpList;
	}

	public Map<Integer, StatsSet> getPetRankList()
	{
		return _petList;
	}

	public Map<Integer, PkPledgeRanking> getClanRankList()
	{
		return _clanList;
	}

	public Map<Integer, PkPledgeRanking> getPreviousClanRankList()
	{
		return _snapshotClanList;
	}

	public Map<Integer, StatsSet> getSnapshotPetList()
	{
		return _snapshotPetList;
	}

	public Map<Integer, PVPRankingRankInfo> getOldPvpRankList()
	{
		return _snapshotPvpList;
	}

	public int getTypeForPacker(Player player, boolean visual)
	{
		if(player == null)
			return 5;
		if(visual)
		{
			int raceRank = getPlayerRaceRank(player);

			if(getPlayerGlobalRank(player) == 1)
			{
				if((raceRank >= 1 && raceRank <= 3) && (player.getClassId() == ClassId.H_DEATH_KNIGHT || player.getClassId() == ClassId.E_DEATH_KNIGHT
						|| player.getClassId() == ClassId.DE_DEATH_KNIGHT))
					return 3;
				return 1;
			}
			else if(raceRank >= 1 && raceRank <= 3)
			{
				return 2;
			}
			else if(getPlayerClassRank(player) == 1 && player.getClassId().getClassLevel() == ClassLevel.THIRD)
				return 4;
			else
				return 0;
		}
		else
		{
			int rank = getPlayerGlobalRank(player);
			if(rank == 1)
				return 1;
			else if(rank <= 30)
				return 2;
			else if(rank <= 100)
				return 3;

			return 0;
		}
	}

	public int getPlayerGlobalRankByChat(Player player)
	{
		if(player == null)
			return 0;
		int rank = getPlayerGlobalRank(player);
		if(rank == 0 || rank > 3)
			return 0;
		return rank;
	}

	public int getPlayerGlobalRank(Player player)
	{
		return _snapshotMainList.values().stream().filter(data -> data.nCharId
				== player.getObjectId()).findFirst().orElse(new PkRankerData(player)).nServerRank;
	}

	public int getPlayerRaceRank(Player player)
	{
		return _snapshotMainList.values().stream().filter(data -> data.nCharId
				== player.getObjectId()).findFirst().orElse(new PkRankerData(player)).nRaceRank;
	}

	public int getPlayerClassRank(Player player)
	{
		return _snapshotMainList.values().stream().filter(data -> data.nCharId
				== player.getObjectId()).findFirst().orElse(new PkRankerData(player)).nClassRank;
	}

	public Map<Integer, StatsSet> getSubjugationRanks(int zoneId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		Map<Integer, StatsSet> result = new ConcurrentHashMap<>();
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SUBJUGATION_RANKING);
			statement.setInt(1, zoneId);
			rset = statement.executeQuery();
			int i = 1;
			while(rset.next())
			{
				StatsSet player = new StatsSet();
				player.set("charId", rset.getInt("character_subjugation.charId"));
				player.set("name", rset.getString("characters.char_name"));
				player.set("points", rset.getInt("total"));
				result.put(i, player);
				i++;
			}
		}
		catch(Exception e)
		{
			LOGGER.error("CharacterVariablesDAO:restore(playerObjId)", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return result;
	}

	private void updateRankEffects()
	{
		for(PkRankerData player : _snapshotMainList.values())
		{
			if(player == null)
				continue;

			int charId = player.nCharId;
			Player plr = GameObjectsStorage.getPlayer(charId);
			if(plr != null)
			{
				// total rank
				if(player.nServerRank < 101)
					replaceServerRankSkills(plr, player.nServerRank);

				// race rank
				int raceRank = player.nRaceRank;
				if(raceRank > 0 && raceRank < 4)
					replaceRaceRankSkills(plr, raceRank);

				replaceClassRankSkills(plr, player.nClassRank);

				plr.broadcastUserInfo(true);
			}
		}
		for(PkRankerScoreData player : _snapshotScoreList.values())
		{
			if(player == null)
				continue;

			int charId = player.nCharId;
			Player plr = GameObjectsStorage.getPlayer(charId);
			if(plr != null)
			{
				replaceServerRankScoreSkills(plr, player.nServerRank);
				plr.broadcastUserInfo(true);
			}
		}
	}

	private void replaceClassRankSkills(Player player, int classRank)
	{
		if(classRank == 1)
			SERVER_RANKING_CLASS.getEffects(player, player);
		else
			player.getAbnormalList().stop(SERVER_RANKING_CLASS, false);
	}

	private void replaceServerRankScoreSkills(Player player, int rank)
	{
		player.removeSkillById(SERVER_SCORE_RANKING_1ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_2ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_3ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_4ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_5ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_6ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_7ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_8ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_9ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_10ST.getId());
		player.removeSkillById(SERVER_SCORE_RANKING_11_100ST.getId());

		switch(rank)
		{
			case 1:
				player.addSkill(SERVER_SCORE_RANKING_1ST, false);
				break;
			case 2:
				player.addSkill(SERVER_SCORE_RANKING_2ST, false);
				break;
			case 3:
				player.addSkill(SERVER_SCORE_RANKING_3ST, false);
				break;
			case 4:
				player.addSkill(SERVER_SCORE_RANKING_4ST, false);
				break;
			case 5:
				player.addSkill(SERVER_SCORE_RANKING_5ST, false);
				break;
			case 6:
				player.addSkill(SERVER_SCORE_RANKING_6ST, false);
				break;
			case 7:
				player.addSkill(SERVER_SCORE_RANKING_7ST, false);
				break;
			case 8:
				player.addSkill(SERVER_SCORE_RANKING_8ST, false);
				break;
			case 9:
				player.addSkill(SERVER_SCORE_RANKING_9ST, false);
				break;
			case 10:
				player.addSkill(SERVER_SCORE_RANKING_10ST, false);
				break;
			default:
				break;
		}

		if(rank > 10 && rank <= 100)
			player.addSkill(SERVER_SCORE_RANKING_11_100ST, false);
	}

	private void replaceServerRankSkills(Player player, int rank)
	{
		if((rank == 0) || (rank > 100))
		{
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_2ND_CLASS, false);
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_3RD_CLASS, false);
		}
		else if(rank == 1)
		{
			SERVER_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_2ND_CLASS, false);
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_3RD_CLASS, false);
		}
		else if(rank <= 30)
		{
			SERVER_LEVEL_RANKING_2ND_CLASS.getEffects(player, player);
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_3RD_CLASS, false);
		}
		else if(rank <= 100)
		{
			SERVER_LEVEL_RANKING_3RD_CLASS.getEffects(player, player);
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(SERVER_LEVEL_RANKING_2ND_CLASS, false);
		}
	}

	private void replaceRaceRankSkills(Player player, int raceRank)
	{
		final ClassId classId = player.getClassId();
		if((raceRank > 0) && (raceRank < 4))
		{
			switch(player.getRace())
			{
				case HUMAN:
				{
					if(raceRank == 1)
						HUMAN_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
					else
						player.getAbnormalList().stop(HUMAN_LEVEL_RANKING_1ST_CLASS, false);

					if((classId != ClassId.H_DEATH_BLADE) && (classId != ClassId.H_DEATH_KNIGHT) && (classId != ClassId.H_DEATH_MESSENGER)
							&& (classId != ClassId.H_DEATH_PILGRIM))
						player.addSkill(HUMAN_RANKING_BENEFIT, false);
					else
						player.addSkill(DEATH_KNIGHT_RANKING_BENEFIT, false);
					break;
				}
				case ELF:
				{
					if(raceRank == 1)
					{
						ELF_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
					}
					else
					{
						player.getAbnormalList().stop(ELF_LEVEL_RANKING_1ST_CLASS, false);
					}
					if((classId != ClassId.E_DEATH_BLADE) && (classId != ClassId.E_DEATH_KNIGHT) && (classId != ClassId.E_DEATH_MESSENGER)
							&& (classId != ClassId.E_DEATH_PILGRIM))
					{
						player.addSkill(ELF_RANKING_BENEFIT, false);
					}
					else
					{
						player.addSkill(DEATH_KNIGHT_RANKING_BENEFIT, false);
					}
					break;
				}
				case DARKELF:
				{
					if(raceRank == 1)
					{
						DARK_ELF_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
					}
					else
					{
						player.getAbnormalList().stop(DARK_ELF_LEVEL_RANKING_1ST_CLASS, false);
					}
					if((classId != ClassId.DE_DEATH_BLADE) && (classId != ClassId.DE_DEATH_KNIGHT) && (classId != ClassId.DE_DEATH_MESSENGER)
							&& (classId != ClassId.DE_DEATH_PILGRIM))
					{
						player.addSkill(DARK_ELF_RANKING_BENEFIT, false);
					}
					else
					{
						player.addSkill(DEATH_KNIGHT_RANKING_BENEFIT, false);
					}
					break;
				}
				case ORC:
				{
					if(raceRank == 1)
					{
						ORC_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
					}
					else
					{
						player.getAbnormalList().stop(ORC_LEVEL_RANKING_1ST_CLASS, false);
					}
					player.addSkill(ORC_RANKING_BENEFIT, false);
					break;
				}
				case DWARF:
				{
					if(raceRank == 1)
					{
						DWARF_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
					}
					else
					{
						player.getAbnormalList().stop(DWARF_LEVEL_RANKING_1ST_CLASS, false);
					}
					player.addSkill(DWARF_RANKING_BENEFIT, false);
					break;
				}
				case KAMAEL:
				{
					if(raceRank == 1)
					{
						KAMAEL_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
					}
					else
					{
						player.getAbnormalList().stop(KAMAEL_LEVEL_RANKING_1ST_CLASS, false);
					}
					player.addSkill(KAMAEL_RANKING_BENEFIT, false);
					break;
				}
				case SYLPH:
				{
					if(raceRank == 1)
					{
						SYLPH_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
					}
					else
					{
						player.getAbnormalList().stop(SYLPH_LEVEL_RANKING_1ST_CLASS, false);
					}
					player.addSkill(SYLPH_RANKING_BENEFIT, false);
					break;
				}
				case highelf:
				{
					if(raceRank == 1)
					{
						HIGHELF_LEVEL_RANKING_1ST_CLASS.getEffects(player, player);
					}
					else
					{
						player.getAbnormalList().stop(HIGHELF_LEVEL_RANKING_1ST_CLASS, false);
					}
					player.addSkill(HIGHELF_RANKING_BENEFIT, false);
					break;
				}
				default:
					break;
			}
		}
		else
		{
			player.getAbnormalList().stop(HUMAN_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(ELF_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(DARK_ELF_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(ORC_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(DWARF_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(KAMAEL_LEVEL_RANKING_1ST_CLASS, false);
			player.getAbnormalList().stop(SYLPH_LEVEL_RANKING_1ST_CLASS, false);
			player.removeSkill(HUMAN_RANKING_BENEFIT, false);
			player.removeSkill(ELF_RANKING_BENEFIT, false);
			player.removeSkill(DARK_ELF_RANKING_BENEFIT, false);
			player.removeSkill(ORC_RANKING_BENEFIT, false);
			player.removeSkill(DWARF_RANKING_BENEFIT, false);
			player.removeSkill(KAMAEL_RANKING_BENEFIT, false);
			player.removeSkill(SYLPH_RANKING_BENEFIT, false);
			player.removeSkill(HIGHELF_RANKING_BENEFIT, false);
			player.removeSkill(DEATH_KNIGHT_RANKING_BENEFIT, false);
		}
	}

	public void onPlayerEnter(Player player)
	{
		final int rank = RankManager.getInstance().getPlayerGlobalRank(player);
		replaceServerRankSkills(player, rank);
		final int raceRank = RankManager.getInstance().getPlayerRaceRank(player);
		replaceRaceRankSkills(player, raceRank);

		replaceClassRankSkills(player, RankManager.getInstance().getPlayerClassRank(player));

		replaceServerRankScoreSkills(player, RankManager.getInstance().getSnapshotScoreData(player).nServerRank);
	}

	public static RankManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder
	{
		protected static final RankManager INSTANCE = new RankManager();
	}

	/** Добавляет игрока в общий рейтинг.
	 *  - ранг сервера               → следующий после максимального существующего ключа;
	 *  - ранг класса (вторая+третья)→ count(игроков той же группы)+1;
	 *  - ранг расы                  → count(игроков той же расы)  +1.
	 */
	public void addNewPlayer(Player p)
	{
		synchronized (_mainList)
		{
			// -------- server rank --------
			int serverRank = _mainList.isEmpty() ? 1 : Collections.max(_mainList.keySet()) + 1;

			// -------- class rank (2-я + 3-я профа) --------
			ClassId classId = p.getClassId();
			int groupKey = RankingGenerateDAO.getClassGroupKey(classId);
			int classRank = (int) _mainList.values().stream().filter(d -> RankingGenerateDAO.getClassGroupKey(ClassId.valueOf(d.nClassId))
					== groupKey).count() + 1;

			// -------- race rank --------
			int raceId = p.getRace().ordinal();
			int raceRank = (int) _mainList.values().stream().filter(d -> d.nRace == raceId).count() + 1;

			String pledgeName = p.getClan() != null ? p.getClan().getName() : "";
			_mainList.put(serverRank, new PkRankerData(p.getObjectId(), p.getName(), pledgeName, p.getLevel(), p.getClassId().getId(), raceId, classRank, raceRank, serverRank));
		}
	}

	public void removePlayerFromAllMaps(int objectId)
	{
		_mainList.entrySet().removeIf(entry -> entry.getValue().nCharId == objectId);
		_mainOlyList.entrySet().removeIf(entry -> entry.getValue().nCharId == objectId);
		_mainPvpList.entrySet().removeIf(entry -> entry.getValue().nCharId == objectId);
		_petList.entrySet().removeIf(entry -> entry.getValue().getInteger("charId") == objectId);

		/*		_snapshotOlyList.entrySet().removeIf(entry -> entry.getValue().getInteger("charId") == objectId);
				_previousOlyList.entrySet().removeIf(entry -> entry.getValue().getInteger("charId") == objectId);
				_snapshotPvpList.entrySet().removeIf(entry -> entry.getValue().getInteger("charId") == objectId);
				_snapshotClanList.entrySet().removeIf(entry -> entry.getValue() == objectId);
				_snapshotPetList.entrySet().removeIf(entry -> entry.getValue().getInteger("charId") == objectId);
				_snapshotMainList.entrySet().removeIf(entry -> entry.getValue().getInteger("charId") == objectId);*/
	}

	public PkRankerData getRankData(Player player)
	{
		return _mainList.values().stream().filter(data -> data.nCharId == player.getObjectId()).findFirst().orElse(new PkRankerData(player));
	}

	public PkRankerData getSnapshotData(Player player)
	{
		return _snapshotMainList.values().stream().filter(data -> data.nCharId == player.getObjectId()).findFirst().orElse(new PkRankerData(player));
	}

	public PVPRankingRankInfo getPvpRankData(Player player)
	{
		return _mainPvpList.values().stream().filter(rankInfo -> rankInfo.nCharId
				== player.getObjectId()).findFirst().orElseGet(() -> new PVPRankingRankInfo(player.getObjectId(), player.getName(), player.getClan()
						!= null ? player.getClan().getName() : "", player.getLevel(), player.getRace().ordinal(), player.getClassId().getId(), 0, // nPVPPoint
						0, // nRank
						0, //nRaceRank
						0, // nKillCount
						0 // nDieCount
		));
	}

	public Map<Integer, PkRankerScoreData> getScoreList()
	{
		return _scoreList;
	}

	public Map<Integer, PkRankerScoreData> getSnapshotScoreList()
	{
		return _snapshotScoreList;
	}

	public PkRankerScoreData getRankScoreData(Player player)
	{
		return _scoreList.values().stream().filter(data -> data.nCharId == player.getObjectId()).findFirst().orElse(new PkRankerScoreData(player));
	}

	public PkRankerScoreData getSnapshotScoreData(Player player)
	{
		return _snapshotScoreList.values().stream().filter(data -> data.nCharId
				== player.getObjectId()).findFirst().orElse(new PkRankerScoreData(player));
	}

	public OlympiadRankInfo getRankOlyData(Player player)
	{
		return _mainOlyList.values().stream().filter(data -> data.nCharId == player.getObjectId()).findFirst().orElse(new OlympiadRankInfo(player));
	}

	public OlympiadRankInfo getSnapshotOlyData(Player player)
	{
		return _previousOlyList.values().stream().filter(data -> data.nCharId == player.getObjectId()).findFirst().orElse(new OlympiadRankInfo(player));
	}

	public OlympiadRankInfo getSnapshotOlyData(int objectId)
	{
		return _previousOlyList.values().stream().filter(data -> data.nCharId == objectId).findFirst().orElse(null);
	}

	public Map<Integer, PkPledgeRanking> getClanRbPointsRankList()
	{
		return _clanRbPointsList;
	}

	public Map<Integer, PkPledgeRanking> getPreviousClanRbPointsRankList()
	{
		return _snapshotClanRbPointsList;
	}
}