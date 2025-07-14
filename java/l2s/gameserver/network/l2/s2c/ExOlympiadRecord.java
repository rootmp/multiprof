package l2s.gameserver.network.l2.s2c;

import java.util.Calendar;
import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.templates.ranking.OlympiadRankInfo;

/**
 * @author nexvill
 */
public class ExOlympiadRecord implements IClientOutgoingPacket
{
	private final Player _player;
	private final Map<Integer, OlympiadRankInfo> _data;

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
		_data = RankManager.getInstance().getPreviousOlyList();

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
	public boolean write(PacketWriter packetWriter)
	{
		int totalRank = 0;
		int totalClassRankers = 0;
		int classRank = 0;
		int points = 0;
		int wins = 0;
		int loses = 0;

		for(int id : _data.keySet())
		{
			OlympiadRankInfo data = _data.get(id);

			if(data.nClassID == _player.getClassId().getId())
			{
				totalClassRankers++;
				if(data.nCharId == _player.getObjectId())
				{
					classRank = totalClassRankers;
				}
			}
			if(data.nCharId == _player.getObjectId())
			{
				totalRank = id;
				points = data.nOlympiadPoint;
				wins = data.nWinCount;
				loses = data.nLoseCount;
			}
		}

		packetWriter.writeD(currCyclePoints);
		packetWriter.writeD(currCycleWins); // Win Count
		packetWriter.writeD(currCycleLosses); // Maybe?? loose count
		packetWriter.writeD(todayFightsLeft);
		packetWriter.writeD(_player.getClassId().getId()); // player class
		packetWriter.writeD(totalRank); // previous cycle rank
		packetWriter.writeD(totalRank > 0 ? _data.size() : 0); // previous cycle total rankers
		packetWriter.writeD(totalRank > 0 ? classRank : 0); // total class rank previous cycle
		packetWriter.writeD(totalRank > 0 ? totalClassRankers : 0); // total class rankers previous cycle
		packetWriter.writeD(totalRank > 0 ? classRank : 0); // server class rank previous cycle
		packetWriter.writeD(totalRank > 0 ? totalClassRankers : 0); // server class rankers previous cycle
		packetWriter.writeD(points); // previous cycle points
		packetWriter.writeD(wins); // previous cycle wins
		packetWriter.writeD(loses); // previous cycle loses
		packetWriter.writeD(Olympiad.getRank(_player));
		packetWriter.writeD(cycleYear); // Cycle Year
		packetWriter.writeD(cycleMonth); // Cycle Month
		packetWriter.writeC(inProgress); // in progress
		packetWriter.writeD(cycle);
		packetWriter.writeC(registered); // is registered
		packetWriter.writeC(Math.min(type, 1)); // 0 - 3x3 Battles, 1 - Olympiad
		return true;
	}
}
