package l2s.gameserver.network.l2.s2c;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Friend;
import l2s.gameserver.templates.ranking.PVPRankingRankInfo;

public class ExPvpRankingList implements IClientOutgoingPacket
{
	private final Player _player;
	private final int nRace;
	private final int bCurrentSeason;
	private final int cRankingGroup;
	private final int cRankingScope;
	private final Map<Integer, PVPRankingRankInfo> _playerList;
	private final Map<Integer, PVPRankingRankInfo> _snapshotList;

	public ExPvpRankingList(Player player, int season, int group, int scope, int race)
	{
		_player = player;

		bCurrentSeason = season;
		cRankingGroup = group;
		cRankingScope = scope;
		nRace = race;

		_playerList = RankManager.getInstance().getPvpRankList();
		_snapshotList = RankManager.getInstance().getOldPvpRankList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(bCurrentSeason);
		packetWriter.writeC(cRankingGroup);
		packetWriter.writeC(cRankingScope);
		packetWriter.writeD(nRace);

		Map<Integer, PVPRankingRankInfo> plrList = (bCurrentSeason == 1) ? _playerList : _snapshotList;

		if(plrList.size() > 0)
		{
			switch(cRankingGroup)
			{
				case 0: // all
					handleRankingAll(packetWriter, plrList);
					break;
				case 1: // race
					handleRankingRace(packetWriter, plrList);
					break;
				case 2: // clan
					handleRankingClan(packetWriter, plrList);
					break;
				case 3: // friend
					handleRankingFriend(packetWriter, plrList);
					break;
				default:
					packetWriter.writeD(0);
					break;
			}
		}
		else
			packetWriter.writeD(0);

		return true;
	}

	private void handleRankingAll(PacketWriter packetWriter, Map<Integer, PVPRankingRankInfo> plrList)
	{
		if(cRankingScope == 0)
		{ // all
			int count = Math.min(plrList.size(), 150);
			packetWriter.writeD(count);

			plrList.values().stream().limit(150).forEach(player -> {
				packetWriter.writeSizedString(player.sCharName);
				packetWriter.writeSizedString(player.sPledgeName);
				packetWriter.writeD(player.nLevel);
				packetWriter.writeD(player.nRace);
				packetWriter.writeD(player.nClass);
				packetWriter.writeQ(player.nPVPPoint); // nPVPPoint
				packetWriter.writeD(player.nRank); // server rank
				packetWriter.writeD(0); // nPrevRank, idk why 0
				packetWriter.writeD(player.nKillCount); // nKillCount
				packetWriter.writeD(player.nDieCount); // nDieCount
			});
		}
		else
		{
			PVPRankingRankInfo player = plrList.values().stream().filter(p -> p.nCharId == _player.getObjectId()).findFirst().orElse(null);
			if(player != null)
			{
				packetWriter.writeSizedString(player.sCharName);
				packetWriter.writeSizedString(player.sPledgeName);
				packetWriter.writeD(player.nLevel);
				packetWriter.writeD(player.nRace);
				packetWriter.writeD(player.nClass);
				packetWriter.writeQ(player.nPVPPoint);
				packetWriter.writeD(player.nRank); // server rank
				packetWriter.writeD(0); // nPrevRank, idk why 0
				packetWriter.writeD(player.nKillCount);
				packetWriter.writeD(player.nDieCount);
			}
			else
				packetWriter.writeD(0);
		}
	}

	private void handleRankingRace(PacketWriter packetWriter, Map<Integer, PVPRankingRankInfo> plrList)
	{
		if(cRankingScope == 0)
		{ // all
			List<PVPRankingRankInfo> racePlayers = plrList.values().stream().filter(player -> player.nRace
					== nRace).limit(100).collect(Collectors.toList());

			packetWriter.writeD(racePlayers.size());

			for(int i = 0; i < racePlayers.size(); i++)
			{
				PVPRankingRankInfo player = racePlayers.get(i);
				packetWriter.writeSizedString(player.sCharName);
				packetWriter.writeSizedString(player.sPledgeName);
				packetWriter.writeD(player.nLevel);
				packetWriter.writeD(player.nRace);
				packetWriter.writeD(player.nClass);
				packetWriter.writeQ(player.nPVPPoint);
				packetWriter.writeD(i + 1); // server rank
				packetWriter.writeD(0); // nPrevRank, idk why 0
				packetWriter.writeD(player.nKillCount);
				packetWriter.writeD(player.nDieCount);
			}
		}
		else
		{
			Map<Integer, PVPRankingRankInfo> raceList = new ConcurrentHashMap<>();
			int i = 1;
			for(PVPRankingRankInfo set : plrList.values())
			{
				if(_player.getRace().ordinal() == set.nRace)
				{
					raceList.put(i, set);
					i++;
				}
			}

			PVPRankingRankInfo player = raceList.values().stream().filter(p -> p.nCharId == _player.getObjectId()).findFirst().orElse(null);
			if(player != null)
			{
				packetWriter.writeSizedString(player.sCharName);
				packetWriter.writeSizedString(player.sPledgeName);
				packetWriter.writeD(player.nLevel);
				packetWriter.writeD(player.nRace);
				packetWriter.writeD(player.nClass);
				packetWriter.writeQ(player.nPVPPoint);
				packetWriter.writeD(player.nRank); // server rank
				packetWriter.writeD(0); // nPrevRank, idk why 0
				packetWriter.writeD(player.nKillCount);
				packetWriter.writeD(player.nDieCount);
			}
			else
				packetWriter.writeD(0);
		}
	}

	private void handleRankingClan(PacketWriter packetWriter, Map<Integer, PVPRankingRankInfo> plrList)
	{
		if(_player.getClan() != null)
		{
			List<PVPRankingRankInfo> clanList = plrList.values().stream().filter(player -> _player.getClan().getName().equals(player.sPledgeName)).collect(Collectors.toList());

			packetWriter.writeD(clanList.size());

			for(int i = 0; i < clanList.size(); i++)
			{
				PVPRankingRankInfo player = clanList.get(i);
				packetWriter.writeSizedString(player.sCharName);
				packetWriter.writeSizedString(player.sPledgeName);
				packetWriter.writeD(player.nLevel);
				packetWriter.writeD(player.nRace);
				packetWriter.writeD(player.nClass);
				packetWriter.writeQ(player.nPVPPoint);
				packetWriter.writeD(i + 1); // server rank
				packetWriter.writeD(0); // nPrevRank, idk why 0
				packetWriter.writeD(player.nKillCount);
				packetWriter.writeD(player.nDieCount);
			}
		}
		else
			packetWriter.writeD(0);
	}

	private void handleRankingFriend(PacketWriter packetWriter, Map<Integer, PVPRankingRankInfo> plrList)
	{
		if(_player.getFriendList().size() > 0)
		{
			Set<Integer> friendList = ConcurrentHashMap.newKeySet();

			for(Friend friend : _player.getFriendList().values())
			{
				plrList.values().stream().filter(player -> player.nCharId == friend.getObjectId()).forEach(player -> friendList.add(player.nCharId));
			}
			friendList.add(_player.getObjectId());

			packetWriter.writeD(friendList.size());

			plrList.values().stream().filter(player -> friendList.contains(player.nCharId)).forEach(player -> {
				packetWriter.writeSizedString(player.sCharName);
				packetWriter.writeSizedString(player.sPledgeName);
				packetWriter.writeD(player.nLevel);
				packetWriter.writeD(player.nRace);
				packetWriter.writeD(player.nClass);
				packetWriter.writeQ(player.nPVPPoint);
				packetWriter.writeD(player.nRank); // server rank
				packetWriter.writeD(0); // nPrevRank, idk why 0
				packetWriter.writeD(player.nKillCount);
				packetWriter.writeD(player.nDieCount);
			});
		}
		else
		{
			PVPRankingRankInfo player = plrList.values().stream().filter(p -> p.nCharId == _player.getObjectId()).findFirst().orElse(null);
			if(player != null)
			{
				packetWriter.writeD(1);
				packetWriter.writeSizedString(player.sCharName);
				packetWriter.writeSizedString(player.sPledgeName);
				packetWriter.writeD(player.nLevel);
				packetWriter.writeD(player.nRace);
				packetWriter.writeD(player.nClass);
				packetWriter.writeQ(player.nPVPPoint);
				packetWriter.writeD(1); // server rank
				packetWriter.writeD(0); // nPrevRank, idk why 0
				packetWriter.writeD(player.nKillCount);
				packetWriter.writeD(player.nDieCount);
			}
			else
				packetWriter.writeD(0);
		}
	}
}
