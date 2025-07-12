package l2s.gameserver.network.l2.s2c;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Friend;
import l2s.gameserver.templates.StatsSet;

public class ExPetRankingList implements IClientOutgoingPacket
{
	private final Player _player;
	private final Map<Integer, StatsSet> _petList;
	private final Map<Integer, StatsSet> _snapshotList;

	private int cRankingGroup;
	private int cRankingScope;
	private int nIndex;
	private int nCollarID;

	public ExPetRankingList(Player player, int group, int scope, int index, int collarID)
	{
		_player = player;
		cRankingGroup = group;
		cRankingScope = scope;
		nIndex = index;
		nCollarID = collarID;

		_petList = RankManager.getInstance().getPetRankList();
		_snapshotList = RankManager.getInstance().getSnapshotPetList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cRankingGroup);
		packetWriter.writeC(cRankingScope);
		packetWriter.writeH(nIndex);
		packetWriter.writeD(nCollarID);

		Map<Integer, StatsSet> plrList;

		if(nIndex == 1)
			plrList = _petList;
		else
			plrList = _snapshotList;

		if(plrList.size() > 0)
		{
			switch(cRankingGroup)
			{
				case 0: // all
				{
					if(cRankingScope == 0) // all
					{
						final int count = plrList.size() > 150 ? 150 : plrList.size();
						packetWriter.writeD(count);
						for(int id : plrList.keySet())
						{
							final StatsSet player = plrList.get(id);
							packetWriter.writeSizedString(player.getString("sNickName"));
							packetWriter.writeSizedString(player.getString("sUserName"));
							packetWriter.writeSizedString(player.getString("sPledgeName"));
							packetWriter.writeD(player.getInteger("nNPCClassID"));
							packetWriter.writeH(player.getInteger("nPetIndex"));
							packetWriter.writeH(player.getInteger("nPetLevel"));
							packetWriter.writeH(player.getInteger("nUserRace"));
							packetWriter.writeH(player.getInteger("nUserLevel"));
							packetWriter.writeD(id); // server rank
							packetWriter.writeD(0); //nPrevRank previous server rank, idk why 0
						}
					}
					else
					{
						boolean found = false;
						for(int id : plrList.keySet())
						{
							final StatsSet player = plrList.get(id);

							if(player.getInteger("player_obj_Id") == _player.getObjectId())
							{
								found = true;
								packetWriter.writeSizedString(player.getString("sNickName"));
								packetWriter.writeSizedString(player.getString("sUserName"));
								packetWriter.writeSizedString(player.getString("sPledgeName"));
								packetWriter.writeD(player.getInteger("nNPCClassID"));
								packetWriter.writeH(player.getInteger("nPetIndex"));
								packetWriter.writeH(player.getInteger("nPetLevel"));
								packetWriter.writeH(player.getInteger("nUserRace"));
								packetWriter.writeH(player.getInteger("nUserLevel"));
								packetWriter.writeD(id); // server rank
								packetWriter.writeD(0); //nPrevRank previous server rank, idk why 0
							}
						}
						if(!found)
						{
							packetWriter.writeD(0);
						}
					}
					break;
				}
				case 1: // вид питомца
				{
					if(cRankingScope == 0) // all
					{
						int count = 0;

						for(int i = 1; i <= plrList.size(); i++)
						{
							final StatsSet pet = plrList.get(i);
							if(nIndex == pet.getInteger("nPetIndex"))
								count++;
						}
						packetWriter.writeD(count > 100 ? 100 : count);

						int i = 1;
						for(int id : plrList.keySet())
						{
							final StatsSet player = plrList.get(id);

							if(nIndex == player.getInteger("nPetIndex"))
							{
								packetWriter.writeSizedString(player.getString("sNickName"));
								packetWriter.writeSizedString(player.getString("sUserName"));
								packetWriter.writeSizedString(player.getString("sPledgeName"));
								packetWriter.writeD(player.getInteger("nNPCClassID"));
								packetWriter.writeH(player.getInteger("nPetIndex"));
								packetWriter.writeH(player.getInteger("nPetLevel"));
								packetWriter.writeH(player.getInteger("nUserRace"));
								packetWriter.writeH(player.getInteger("nUserLevel"));
								packetWriter.writeD(i); // server rank
								packetWriter.writeD(0); //nPrevRank previous server rank, idk why 0
								i++;
							}
						}
					}
					else
					{
						boolean found = false;

						final Map<Integer, StatsSet> raceList = new ConcurrentHashMap<>();
						int i = 1;
						for(int id : plrList.keySet())
						{
							final StatsSet set = plrList.get(id);

							if(_player.getRace().ordinal() == set.getInteger("nPetIndex"))
							{
								raceList.put(i, plrList.get(id));
								i++;
							}
						}

						for(int id : raceList.keySet())
						{
							final StatsSet player = raceList.get(id);

							if(player.getInteger("player_obj_Id") == _player.getObjectId())
							{
								found = true;
								packetWriter.writeSizedString(player.getString("sNickName"));
								packetWriter.writeSizedString(player.getString("sUserName"));
								packetWriter.writeSizedString(player.getString("sPledgeName"));
								packetWriter.writeD(player.getInteger("nNPCClassID"));
								packetWriter.writeH(player.getInteger("nPetIndex"));
								packetWriter.writeH(player.getInteger("nPetLevel"));
								packetWriter.writeH(player.getInteger("nUserRace"));
								packetWriter.writeH(player.getInteger("nUserLevel"));
								packetWriter.writeD(id); // server rank
								packetWriter.writeD(0); //nPrevRank previous server rank, idk why 0
							}
						}
						if(!found)
						{
							packetWriter.writeD(0);
						}
					}
					break;
				}
				case 2: // clan
				{
					if(_player.getClan() != null)
					{
						final Map<Integer, StatsSet> clanList = new ConcurrentHashMap<>();
						int i = 1;
						for(int id : plrList.keySet())
						{
							final StatsSet set = plrList.get(id);

							if(_player.getClan().getName() == set.getString("sPledgeName"))
							{
								clanList.put(i, plrList.get(id));
								i++;
							}
						}

						packetWriter.writeD(clanList.size());

						for(int id : clanList.keySet())
						{
							final StatsSet player = clanList.get(id);

							packetWriter.writeSizedString(player.getString("sNickName"));
							packetWriter.writeSizedString(player.getString("sUserName"));
							packetWriter.writeSizedString(player.getString("sPledgeName"));
							packetWriter.writeD(player.getInteger("nNPCClassID"));
							packetWriter.writeH(player.getInteger("nPetIndex"));
							packetWriter.writeH(player.getInteger("nPetLevel"));
							packetWriter.writeH(player.getInteger("nUserRace"));
							packetWriter.writeH(player.getInteger("nUserLevel"));
							packetWriter.writeD(id); // server rank
							packetWriter.writeD(0); //nPrevRank previous server rank, idk why 0
						}
					}
					else
					{
						packetWriter.writeD(0);
					}

					break;
				}
				case 3: // friend
				{
					if(_player.getFriendList().size() > 0)
					{
						final Set<Integer> friendList = ConcurrentHashMap.newKeySet();
						int count = 1;
						for(Friend friend : _player.getFriendList().values())
						{
							for(Integer id2 : plrList.keySet())
							{
								final StatsSet temp = plrList.get(id2);
								if(temp.getInteger("player_obj_Id") == friend.getObjectId())
								{
									friendList.add(temp.getInteger("player_obj_Id"));
									count++;
								}
							}
						}
						friendList.add(_player.getObjectId());

						packetWriter.writeD(count);

						for(int id : plrList.keySet())
						{
							final StatsSet player = plrList.get(id);
							int i = 1;
							if(friendList.contains(player.getInteger("player_obj_Id")))
							{
								packetWriter.writeSizedString(player.getString("sNickName"));
								packetWriter.writeSizedString(player.getString("sUserName"));
								packetWriter.writeSizedString(player.getString("sPledgeName"));
								packetWriter.writeD(player.getInteger("nNPCClassID"));
								packetWriter.writeH(player.getInteger("nPetIndex"));
								packetWriter.writeH(player.getInteger("nPetLevel"));
								packetWriter.writeH(player.getInteger("nUserRace"));
								packetWriter.writeH(player.getInteger("nUserLevel"));
								packetWriter.writeD(i); // server rank
								packetWriter.writeD(0); //nPrevRank previous server rank, idk why 0
								i++;
							}
						}
					}
					else
					{
						if(plrList.size() > 0)
						{
							for(int id : plrList.keySet())
							{
								final StatsSet player = plrList.get(id);
								if(_player.getObjectId() == player.getInteger("player_obj_Id"))
								{
									packetWriter.writeD(1);
									packetWriter.writeSizedString(player.getString("name"));
									packetWriter.writeSizedString(player.getString("clanName"));
									packetWriter.writeD(player.getInteger("level"));
									packetWriter.writeD(player.getInteger("race"));
									packetWriter.writeD(player.getInteger("classId"));
									packetWriter.writeQ(player.getInteger("points"));
									packetWriter.writeD(1); // server rank
									packetWriter.writeD(0); // previous server rank, idk why 0
									packetWriter.writeD(player.getInteger("kills"));
									packetWriter.writeD(player.getInteger("deaths"));
								}
							}
						}
						else
						{
							packetWriter.writeD(0);
						}
					}
					break;
				}
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
}