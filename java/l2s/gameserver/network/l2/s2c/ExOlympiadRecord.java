package l2s.gameserver.network.l2.s2c;

import java.util.Calendar;
import java.util.Map;

import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class ExOlympiadRecord extends L2GameServerPacket
{
	private final Player _player;
	private final Map<Integer, StatsSet> _playerList;

	private final int currCyclePoints;
	private final int currCycleWins;
	private final int currCycleLosses;
	private final int todayFightsLeft;
	private final int cycleYear;
	private final int cycleMonth;
	private final boolean inProgress;
	private final int cycle;
	private final boolean registered;
	private final int type;

	public ExOlympiadRecord(Player player)
	{
		_player = player;
		_playerList = RankManager.getInstance().getPreviousOlyList();

		currCyclePoints = Olympiad.getParticipantPoints(player.getObjectId());
		currCycleWins = Olympiad.getCompetitionWin(player.getObjectId());
		currCycleLosses = Olympiad.getCompetitionLoose(player.getObjectId());
		todayFightsLeft = Olympiad.getGamesLeft(player.getObjectId());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Olympiad.getOlympiadPeriodStartTime());
		cycleYear = calendar.get(Calendar.YEAR);
		cycleMonth = calendar.get(Calendar.MONTH);
		inProgress = Olympiad.inCompPeriod();
		cycle = Olympiad.getCurrentCycle();
		registered = Olympiad.isRegistered(player, true);
		type = Olympiad.getCompType().ordinal();
	}

	@Override
	protected void writeImpl()
	{
		int totalRank = 0;
		int totalClassRankers = 0;
		int classRank = 0;
		int points = 0;
		int wins = 0;
		int loses = 0;

		for (int id : _playerList.keySet())
		{
			StatsSet player = _playerList.get(id);

			if (player.getInteger("classId") == _player.getClassId().getId())
			{
				totalClassRankers++;
				if (player.getInteger("objId") == _player.getObjectId())
				{
					classRank = totalClassRankers;
				}
			}
			if (player.getInteger("objId") == _player.getObjectId())
			{
				totalRank = id;
				points = player.getInteger("olympiad_points");
				wins = player.getInteger("competitions_win");
				loses = player.getInteger("competitions_lost");
			}
		}

		writeD(currCyclePoints);
		writeD(currCycleWins); // Win Count
		writeD(currCycleLosses); // Maybe?? loose count
		writeD(todayFightsLeft);
		writeD(_player.getClassId().getId()); // player class
		writeD(totalRank); // previous cycle rank
		writeD(totalRank > 0 ? _playerList.size() : 0); // previous cycle total rankers
		writeD(totalRank > 0 ? classRank : 0); // total class rank previous cycle
		writeD(totalRank > 0 ? totalClassRankers : 0); // total class rankers previous cycle
		writeD(totalRank > 0 ? classRank : 0); // server class rank previous cycle
		writeD(totalRank > 0 ? totalClassRankers : 0); // server class rankers previous cycle
		writeD(points); // previous cycle points
		writeD(wins); // previous cycle wins
		writeD(loses); // previous cycle loses
		writeD(Olympiad.getRank(_player));
		writeD(cycleYear); // Cycle Year
		writeD(cycleMonth); // Cycle Month
		writeC(inProgress); // in progress
		writeD(cycle);
		writeC(registered); // is registered
		writeC(Math.min(type, 1)); // 0 - 3x3 Battles, 1 - Olympiad
	}
}
