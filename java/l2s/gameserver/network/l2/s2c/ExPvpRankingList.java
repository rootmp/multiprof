package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Friend;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class ExPvpRankingList implements IClientOutgoingPacket
{
	private final Player _player;
	private final int _race;
	private final int _season;
	private final int _group;
	private final int _scope;
	private final Map<Integer, StatsSet> _playerList;
	private final Map<Integer, StatsSet> _snapshotList;

	public ExPvpRankingList(Player player, int season, int group, int scope, int race)
	{
		_player = player;

		_season = season;
		_group = group;
		_scope = scope;
		_race = race;

		_playerList = RankManager.getInstance().getPvpRankList();
		_snapshotList = RankManager.getInstance().getOldPvpRankList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_season);
		packetWriter.writeC(_group);
		packetWriter.writeC(_scope);
		packetWriter.writeD(_race);

		Map<Integer, StatsSet> plrList;
		if (_season == 1)
		{
			plrList = _playerList;
		}
		else
		{
			plrList = _snapshotList;
		}

		if (plrList.size() > 0)
		{
			switch (_group)
			{
				case 0: // all
				{
					if (_scope == 0) // all
					{
						final int count = plrList.size() > 150 ? 150 : plrList.size();

						packetWriter.writeD(count);

						for (int id : plrList.keySet())
						{
							final StatsSet player = plrList.get(id);

							packetWriter.writeString(player.getString("name"));
							packetWriter.writeString(player.getString("clanName"));
							packetWriter.writeD(player.getInteger("level"));
							packetWriter.writeD(player.getInteger("race"));
							packetWriter.writeD(player.getInteger("classId"));
							packetWriter.writeD(player.getInteger("points"));
							packetWriter.writeD(0); // unk
							packetWriter.writeD(id); // server rank
							packetWriter.writeD(0); // previous server rank, idk why 0
							packetWriter.writeD(player.getInteger("kills"));
							packetWriter.writeD(player.getInteger("deaths"));
						}
					}
					else
					{
						boolean found = false;
						for (int id : plrList.keySet())
						{
							final StatsSet player = plrList.get(id);

							if (player.getInteger("charId") == _player.getObjectId())
							{
								found = true;
								packetWriter.writeString(player.getString("name"));
								packetWriter.writeString(player.getString("clanName"));
								packetWriter.writeD(player.getInteger("level"));
								packetWriter.writeD(player.getInteger("race"));
								packetWriter.writeD(player.getInteger("classId"));
								packetWriter.writeD(player.getInteger("points"));
								packetWriter.writeD(0); // unk
								packetWriter.writeD(id); // server rank
								packetWriter.writeD(0); // previous server rank, idk why 0
								packetWriter.writeD(player.getInteger("kills"));
								packetWriter.writeD(player.getInteger("deaths"));
							}
						}
						if (!found)
						{
							packetWriter.writeD(0);
						}
					}
					break;
				}
				case 1: // race
				{
					if (_scope == 0) // all
					{
						int count = 0;

						for (int i = 1; i <= plrList.size(); i++)
						{
							final StatsSet player = plrList.get(i);
							if (_race == player.getInteger("race"))
							{
								count++;
							}
						}
						packetWriter.writeD(count > 100 ? 100 : count);

						int i = 1;
						for (int id : plrList.keySet())
						{
							final StatsSet player = plrList.get(id);

							if (_race == player.getInteger("race"))
							{
								packetWriter.writeString(player.getString("name"));
								packetWriter.writeString(player.getString("clanName"));
								packetWriter.writeD(player.getInteger("level"));
								packetWriter.writeD(player.getInteger("race"));
								packetWriter.writeD(player.getInteger("classId"));
								packetWriter.writeD(player.getInteger("points"));
								packetWriter.writeD(0); // unk
								packetWriter.writeD(i); // server rank
								packetWriter.writeD(0); // previous server rank, idk why 0
								packetWriter.writeD(player.getInteger("kills"));
								packetWriter.writeD(player.getInteger("deaths"));
								i++;
							}
						}
					}
					else
					{
						boolean found = false;

						final Map<Integer, StatsSet> raceList = new ConcurrentHashMap<>();
						int i = 1;
						for (int id : plrList.keySet())
						{
							final StatsSet set = plrList.get(id);

							if (_player.getRace().ordinal() == set.getInteger("race"))
							{
								raceList.put(i, plrList.get(id));
								i++;
							}
						}

						for (int id : raceList.keySet())
						{
							final StatsSet player = raceList.get(id);

							if (player.getInteger("charId") == _player.getObjectId())
							{
								found = true;
								packetWriter.writeString(player.getString("name"));
								packetWriter.writeString(player.getString("clanName"));
								packetWriter.writeD(player.getInteger("level"));
								packetWriter.writeD(player.getInteger("race"));
								packetWriter.writeD(player.getInteger("classId"));
								packetWriter.writeD(player.getInteger("points"));
								packetWriter.writeD(0); // unk
								packetWriter.writeD(id); // server rank
								packetWriter.writeD(0); // previous server rank, idk why 0
								packetWriter.writeD(player.getInteger("kills"));
								packetWriter.writeD(player.getInteger("deaths"));
							}
						}
						if (!found)
						{
							packetWriter.writeD(0);
						}
					}
					break;
				}
				case 2: // clan
				{
					if (_player.getClan() != null)
					{
						final Map<Integer, StatsSet> clanList = new ConcurrentHashMap<>();
						int i = 1;
						for (int id : plrList.keySet())
						{
							final StatsSet set = plrList.get(id);

							if (_player.getClan().getName() == set.getString("clanName"))
							{
								clanList.put(i, plrList.get(id));
								i++;
							}
						}

						packetWriter.writeD(clanList.size());

						for (int id : clanList.keySet())
						{
							final StatsSet player = clanList.get(id);

							packetWriter.writeString(player.getString("name"));
							packetWriter.writeString(player.getString("clanName"));
							packetWriter.writeD(player.getInteger("level"));
							packetWriter.writeD(player.getInteger("race"));
							packetWriter.writeD(player.getInteger("classId"));
							packetWriter.writeD(player.getInteger("points"));
							packetWriter.writeD(0); // unk
							packetWriter.writeD(id); // server rank
							packetWriter.writeD(0); // previous server rank, idk why 0
							packetWriter.writeD(player.getInteger("kills"));
							packetWriter.writeD(player.getInteger("deaths"));
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
					if (_player.getFriendList().size() > 0)
					{
						final Set<Integer> friendList = ConcurrentHashMap.newKeySet();
						int count = 1;
						for (Friend friend : _player.getFriendList().values())
						{
							for (Integer id2 : plrList.keySet())
							{
								final StatsSet temp = plrList.get(id2);
								if (temp.getInteger("charId") == friend.getObjectId())
								{
									friendList.add(temp.getInteger("charId"));
									count++;
								}
							}
						}
						friendList.add(_player.getObjectId());

						packetWriter.writeD(count);

						for (int id : plrList.keySet())
						{
							final StatsSet player = plrList.get(id);
							int i = 1;
							if (friendList.contains(player.getInteger("charId")))
							{
								packetWriter.writeString(player.getString("name"));
								packetWriter.writeString(player.getString("clanName"));
								packetWriter.writeD(player.getInteger("level"));
								packetWriter.writeD(player.getInteger("race"));
								packetWriter.writeD(player.getInteger("classId"));
								packetWriter.writeD(player.getInteger("points"));
								packetWriter.writeD(0); // unk
								packetWriter.writeD(i); // server rank
								packetWriter.writeD(0); // previous server rank, idk why 0
								packetWriter.writeD(player.getInteger("kills"));
								packetWriter.writeD(player.getInteger("deaths"));
								i++;
							}
						}
					}
					else
					{
						if (plrList.size() > 0)
						{
							for (int id : plrList.keySet())
							{
								final StatsSet player = plrList.get(id);
								if (_player.getObjectId() == player.getInteger("charId"))
								{
									packetWriter.writeD(1);
									packetWriter.writeString(player.getString("name"));
									packetWriter.writeString(player.getString("clanName"));
									packetWriter.writeD(player.getInteger("level"));
									packetWriter.writeD(player.getInteger("race"));
									packetWriter.writeD(player.getInteger("classId"));
									packetWriter.writeD(player.getInteger("points"));
									packetWriter.writeD(0); // unk
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
			}
		}
		else
		{
			packetWriter.writeD(0);
		}
	}
}