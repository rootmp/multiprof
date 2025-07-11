package l2s.gameserver.network.l2.s2c;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Friend;
import l2s.gameserver.templates.ranking.PkRankerData;

public class ExRankingCharRankers implements IClientOutgoingPacket
{
	private final Player _player;
	private final int nRace;
	private final int nClass;
	private final int cRankingGroup;
	private final int cRankingScope;
	private final Map<Integer, PkRankerData> _mainList;
	private final Map<Integer, PkRankerData> _snapshotList;

	public ExRankingCharRankers(Player player, int group, int scope, int race, int classId)
	{
		_player = player;
		cRankingGroup = group;
		cRankingScope = scope;
		nRace = race;
		nClass = classId;
		_mainList = RankManager.getInstance().getRankList();
		_snapshotList = RankManager.getInstance().getSnapshotList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cRankingGroup);
		packetWriter.writeC(cRankingScope);

		packetWriter.writeD(nRace);
		packetWriter.writeD(nClass);

		if(_mainList.size() > 0)
		{
			switch(cRankingGroup)
			{
				case 0:
					handleRankingAll(packetWriter);
					break;
				case 1:
					handleRankingRace(packetWriter);
					break;
				case 2:
					handleRankingClan(packetWriter);
					break;
				case 3:
					handleRankingFriend(packetWriter);
					break;
				case 4:
					handleRankingClass(packetWriter);
					break;
				default:
					break;
			}
		}
		else
		{
			packetWriter.writeD(0);
		}
		return true;
	}

	private void handleRankingAll(PacketWriter packetWriter)
	{
		if(cRankingScope == 0)
		{
			List<Map.Entry<Integer, PkRankerData>> limitedList = _mainList.entrySet().stream().limit(100).collect(Collectors.toList());

			packetWriter.writeD(limitedList.size());

			for(Map.Entry<Integer, PkRankerData> entry : limitedList)
			{
				writePlayerData(packetWriter, entry.getValue(), entry.getKey());
				writeSnapshotData(packetWriter, entry.getValue().nCharId);
			}
		}
		else
		{
			int playerId = _player.getObjectId();
			Optional<Map.Entry<Integer, PkRankerData>> playerEntry = _mainList.entrySet().stream().filter(entry -> entry.getValue().nCharId
					== playerId).findFirst();

			if(playerEntry.isPresent())
			{
				int id = playerEntry.get().getKey();
				int first = Math.max(id - 9, 1);
				int last = Math.min(id + 10, _mainList.size());
				int count = last - first + 1;
				packetWriter.writeD(count);

				for(int i = first; i <= last; i++)
				{
					PkRankerData plr = _mainList.get(i);
					writePlayerData(packetWriter, plr, i);
					writeSnapshotData(packetWriter, plr.nCharId);
				}
			}
			else
				packetWriter.writeD(0);

		}
	}

	private void handleRankingRace(PacketWriter packetWriter)
	{
		if(cRankingScope == 0)
		{
			List<PkRankerData> racePlayers = _mainList.values().stream().filter(player -> nRace == player.nRace).limit(100).collect(Collectors.toList());

			packetWriter.writeD(racePlayers.size());

			int i = 1;
			for(PkRankerData player : racePlayers)
			{
				writePlayerData(packetWriter, player, i);
				writeSnapshotData(packetWriter, player.nCharId);
				i++;
			}
		}
		else
		{
			Map<Integer, PkRankerData> raceList = new LinkedHashMap<>();
			int i = 1;
			for(PkRankerData player : _mainList.values())
			{
				if(_player.getRace().ordinal() == player.nRace)
				{
					raceList.put(i, player);
					i++;
				}
			}

			boolean found = false;
			for(Map.Entry<Integer, PkRankerData> entry : raceList.entrySet())
			{
				int id = entry.getKey();
				PkRankerData data = entry.getValue();
				if(data.nCharId == _player.getObjectId())
				{
					found = true;
					int first = Math.max(id - 9, 1);
					int last = Math.min(id + 10, raceList.size());
					int count = last - first + 1;
					packetWriter.writeD(count);
					for(int id2 = first; id2 <= last; id2++)
					{
						PkRankerData plr = raceList.get(id2);
						writePlayerData(packetWriter, plr, id2);
						writeSnapshotData(packetWriter, plr.nCharId);
					}
					break;
				}
			}

			if(!found)
				packetWriter.writeD(0);
		}
	}

	private void handleRankingClan(PacketWriter packetWriter)
	{
		if(_player.getClan() != null)
		{
			String clanName = _player.getClan().getName();

			List<Map.Entry<Integer, PkRankerData>> clanList = _mainList.entrySet().stream().filter(entry -> clanName.equalsIgnoreCase(entry.getValue().sPledgeName)).collect(Collectors.toList());

			packetWriter.writeD(clanList.size());

			int i = 1;
			for(Map.Entry<Integer, PkRankerData> entry : clanList)
			{
				writePlayerData(packetWriter, entry.getValue(), i);
				writeSnapshotData(packetWriter, entry.getValue().nCharId);
				i++;
			}
		}
		else
			packetWriter.writeD(0);
	}

	private void handleRankingFriend(PacketWriter packetWriter)
	{
		if(_player.getFriendList().size() > 0)
		{
			final Set<Integer> friendList = ConcurrentHashMap.newKeySet();
			int count = 1;
			for(Friend friend : _player.getFriendList().values())
			{
				for(Integer id2 : _mainList.keySet())
				{
					final PkRankerData temp = _mainList.get(id2);
					if(temp.nCharId == friend.getObjectId())
					{
						friendList.add(temp.nCharId);
						count++;
					}
				}
			}
			friendList.add(_player.getObjectId());

			packetWriter.writeD(count);

			for(int id : _mainList.keySet())
			{
				final PkRankerData player = _mainList.get(id);
				if(friendList.contains(player.nCharId))
				{
					writePlayerData(packetWriter, player, id);

					if(_snapshotList.size() > 0)
					{
						for(int id2 : _snapshotList.keySet())
						{
							final PkRankerData snapshot = _snapshotList.get(id2);

							if(player.nCharId == snapshot.nCharId)
							{
								packetWriter.writeD(id2); // server rank snapshot
								packetWriter.writeD(snapshot.nRaceRank); // race rank snapshot
								packetWriter.writeD(5316850);
							}
						}
					}
					else
					{
						packetWriter.writeD(id);
						packetWriter.writeD(0);
						packetWriter.writeD(5316850);
					}
				}
			}
		}
		else
		{
			packetWriter.writeD(1);
			packetWriter.writeSizedString(_player.getName());
			if(_player.getClan() != null)
				packetWriter.writeSizedString(_player.getClan().getName());
			else
				packetWriter.writeSizedString("");

			packetWriter.writeD(0);//TODO 388 nWorldID
			packetWriter.writeD(_player.getBaseSubClass().getLevel()); //level
			packetWriter.writeD(_player.getBaseSubClass().getClassId());
			packetWriter.writeD(_player.getRace().ordinal());
			packetWriter.writeD(1); // clan rank

			writeSnapshotData(packetWriter, _player.getObjectId());
		}
	}

	private void handleRankingClass(PacketWriter packetWriter)
	{
		if(cRankingScope == 0)
		{
			List<PkRankerData> classPlayers = _mainList.values().stream().filter(data -> nClass == data.nClassId).limit(100).collect(Collectors.toList());

			packetWriter.writeD(classPlayers.size());

			int i = 1;
			for(PkRankerData data : classPlayers)
			{
				writePlayerData(packetWriter, data, i);
				writeSnapshotData(packetWriter, data.nCharId);
				i++;
			}
		}
		else
		{
			Map<Integer, PkRankerData> classList = new ConcurrentHashMap<>();
			int i = 1;
			for(int id : _mainList.keySet())
			{
				PkRankerData data = _mainList.get(id);
				if(_player.getClassId().getId() == data.nClassId)
				{
					classList.put(i, _mainList.get(id));
					i++;
				}
			}
			int playerId = _player.getObjectId();
			Optional<Map.Entry<Integer, PkRankerData>> playerEntry = classList.entrySet().stream().filter(entry -> entry.getValue().nCharId
					== playerId).findFirst();

			if(playerEntry.isPresent())
			{
				int id = playerEntry.get().getKey();
				int first = Math.max(id - 9, 1);
				int last = Math.min(id + 10, classList.size());
				int count = last - first + 1;
				packetWriter.writeD(count);

				for(int j = first; j <= last; j++)
				{
					PkRankerData data = classList.get(j);
					writePlayerData(packetWriter, data, j);
					writeSnapshotData(packetWriter, data.nCharId);
				}
			}
			else

				packetWriter.writeD(0);

		}
	}

	private void writePlayerData(PacketWriter packetWriter, PkRankerData data, int nRank)
	{
		packetWriter.writeSizedString(data.sUserName); // sUserName
		packetWriter.writeSizedString(data.sPledgeName); //sPledgeName 
		packetWriter.writeD(0); // nWorldID
		packetWriter.writeD(data.nLevel); // nLevel
		packetWriter.writeD(data.nClassId);// nClass
		packetWriter.writeD(data.nRace);//nRace
		packetWriter.writeD(nRank); // nRank
	}

	private void writeSnapshotData(PacketWriter packetWriter, int objectId)
	{
		_snapshotList.entrySet().stream().filter(entry -> entry.getValue().nCharId == objectId).findFirst().ifPresentOrElse(entry -> {
			PkRankerData snapshot = entry.getValue();
			packetWriter.writeD(snapshot.nServerRank); //nServerRank_Snapshot
			packetWriter.writeD(snapshot.nRaceRank); //nRaceRank_Snapshot
			packetWriter.writeD(snapshot.nClassRank);//packet.nClassRank_Snapshot
		}, () -> {
			packetWriter.writeD(0);
			packetWriter.writeD(0);
			packetWriter.writeD(0);
		});
	}
}
