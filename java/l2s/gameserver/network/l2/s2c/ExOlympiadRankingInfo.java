package l2s.gameserver.network.l2.s2c;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.ranking.OlympiadRankInfo;

public class ExOlympiadRankingInfo implements IClientOutgoingPacket
{
	private final Player _player;
	private final int cRankingType;
	private final int cRankingScope;
	private final int bCurrentSeason;
	private final int nClassID;
	private final int nWorldID;
	private int nElectionMatchCount;
	
	private final Map<Integer, OlympiadRankInfo> _playerList;
	private final Map<Integer, OlympiadRankInfo> _snapshotList;


	public ExOlympiadRankingInfo(Player player, int cRankingType, int cRankingScope, int bCurrentSeason, int nClassID, int nWorldID)
	{
		_player = player;
		this.cRankingType = cRankingType;
		this.cRankingScope = cRankingScope;
		this.bCurrentSeason = bCurrentSeason;
		this.nClassID = nClassID;
		this.nWorldID = nWorldID;
		_playerList = RankManager.getInstance().getOlyRankList();
		_snapshotList = RankManager.getInstance().getPreviousOlyList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cRankingType);
		packetWriter.writeC(cRankingScope);
		packetWriter.writeC(bCurrentSeason);
		packetWriter.writeD(nClassID);
		packetWriter.writeD(nWorldID); // 0 - all servers, server id - for caller server
		packetWriter.writeD(_playerList.size()); // nTotalUser, 933 all time
		packetWriter.writeD(nElectionMatchCount);

		if(_playerList.size() > 0)
		{
			switch(cRankingType)
			{
				case 0:
					handleAllRanking(packetWriter);
					break;
				case 1:
					handleClassRanking(packetWriter);
					break;
				default:
					break;
			}
		}
		else
			packetWriter.writeD(0);
		return true;
	}

	private void handleAllRanking(PacketWriter packetWriter)
	{
		if(cRankingScope == 0)
		{
			packetWriter.writeD(Math.min(_playerList.size(), 100));
			_playerList.entrySet().stream().limit(100).forEach(entry -> writePlayerInfo(packetWriter, entry.getValue(), entry.getKey()));
		}
		else
		{
			boolean found = false;
			for(Map.Entry<Integer, OlympiadRankInfo> entry : _playerList.entrySet())
			{
				if(entry.getValue().nCharId == _player.getObjectId())
				{
					found = true;
					writeSurroundingRanks(packetWriter, entry.getKey());
					break;
				}
			}
			if(!found)
			{
				packetWriter.writeD(0);
			}
		}
	}

	private void handleClassRanking(PacketWriter packetWriter)
	{
		Map<Integer, OlympiadRankInfo> classFiltered = _playerList.entrySet().stream().filter(entry -> entry.getValue().nClassID
				== nClassID).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		if(cRankingScope == 0)
		{
			List<Map.Entry<Integer, OlympiadRankInfo>> sortedClassList = classFiltered.entrySet().stream().sorted(Map.Entry.comparingByValue((a, b) -> Integer.compare(a.nClassRank, b.nClassRank))).limit(50).collect(Collectors.toList());

			packetWriter.writeD(sortedClassList.size());
			sortedClassList.forEach(entry -> writePlayerInfo(packetWriter, entry.getValue(), entry.getValue().nClassRank));
		}
		else
		{
			boolean found = false;
			List<Map.Entry<Integer, OlympiadRankInfo>> sortedClassList = classFiltered.entrySet().stream().sorted(Map.Entry.comparingByValue((a, b) -> Integer.compare(a.nClassRank, b.nClassRank))).collect(Collectors.toList());

			for(Map.Entry<Integer, OlympiadRankInfo> entry : sortedClassList)
			{
				if(entry.getValue().nCharId == _player.getObjectId())
				{
					found = true;
					writeSurroundingRanks(packetWriter, entry.getValue().nClassRank);
					break;
				}
			}
			if(!found)
			{
				packetWriter.writeD(0);
			}
		}
	}

	private void writePlayerInfo(PacketWriter packetWriter, OlympiadRankInfo player, int rank)
	{
		packetWriter.writeSizedString(player.sCharName);
		packetWriter.writeSizedString(player.sPledgeName);
		packetWriter.writeD(rank); // nRank
		
		// nPrevRank
		int prevRank = 0; 
		if (_snapshotList.size() > 0)
		{
			prevRank = _snapshotList.entrySet().stream()
				.filter(entry -> entry.getValue().nCharId == player.nCharId)
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(0);
		}
		packetWriter.writeD(prevRank);
		
		packetWriter.writeD(player.nElectionRank); //nElectionRank
		packetWriter.writeD(Config.REQUEST_ID); // nWorldID
		packetWriter.writeD(player.nLevel); // nLevel
		packetWriter.writeD(player.nClassID); // nClassID
		packetWriter.writeD(player.nPledgeLevel); // nPledgeLevel
		packetWriter.writeD(player.nWinCount); // nWinCount
		packetWriter.writeD(player.nLoseCount); // nLoseCount
		packetWriter.writeD(player.nDrawCount);//nDrawCount
		packetWriter.writeD(player.nOlympiadPoint); // nOlympiadPoint
		packetWriter.writeD(player.nHeroCount); // nHeroCount
		packetWriter.writeD(player.nLegendCount); // nLegendCount
	}

	private void writeSurroundingRanks(PacketWriter packetWriter, int rank)
	{
		int first = Math.max(1, rank - 9);
		int last = Math.min(_playerList.size(), rank + 10);

		packetWriter.writeD(last - first + 1);
		for(int i = first; i <= last; i++)
		{
			OlympiadRankInfo player = _playerList.get(i);
			writePlayerInfo(packetWriter, player, i);
		}
	}
}
