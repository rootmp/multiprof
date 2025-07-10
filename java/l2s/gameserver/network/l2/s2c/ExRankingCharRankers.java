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
public class ExRankingCharRankers implements IClientOutgoingPacket
{
	private final Player _player;
	private final int _race;
	private final int _group;
	private final int _scope;
	private final int _class;
	private final Map<Integer, StatsSet> _playerList;
	private final Map<Integer, StatsSet> _snapshotList;

	public ExRankingCharRankers(Player player, int group, int scope, int race)
	{
		_player = player;

		_group = group;
		_scope = scope;
		_race = race;
		_class = 0;

		_playerList = RankManager.getInstance().getRankList();
		_snapshotList = RankManager.getInstance().getSnapshotList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_group);
		packetWriter.writeC(_scope);
		packetWriter.writeD(_race);
		packetWriter.writeD(_class);

		if (_playerList.size() > 0)
		{
			switch (_group)
			{
				case 0: // all
				{
					if (_scope == 0) // all
					{
						final int count = _playerList.size() > 150 ? 150 : _playerList.size();

						packetWriter.writeD(count);

						for (int id : _playerList.keySet())
						{
							final StatsSet player = _playerList.get(id);

							packetWriter.writeString(player.getString("name"));
							packetWriter.writeString(player.getString("clanName"));
							packetWriter.writeD(player.getInteger("level"));
							packetWriter.writeD(player.getInteger("classId"));
							packetWriter.writeD(player.getInteger("race"));
							packetWriter.writeD(id); // server rank
							if (_snapshotList.size() > 0)
							{
								for (int id2 : _snapshotList.keySet())
								{
									final StatsSet snapshot = _snapshotList.get(id2);

									if (player.getInteger("charId") == snapshot.getInteger("charId"))
									{
										packetWriter.writeD(id2); // server rank snapshot
										packetWriter.writeD(snapshot.getInteger("raceRank", 0)); // race rank snapshot
										packetWriter.writeD(5316850); // unk, value from JP
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
					else
					{
						boolean found = false;
						for (int id : _playerList.keySet())
						{
							final StatsSet player = _playerList.get(id);

							if (player.getInteger("charId") == _player.getObjectId())
							{
								found = true;
								final int first = id > 10 ? (id - 9) : 1;
								final int last = _playerList.size() >= (id + 10) ? id + 10 : id + (_playerList.size() - id);
								if (first == 1)
								{
									packetWriter.writeD(last - (first - 1));
								}
								else
								{
									packetWriter.writeD(last - first);
								}
								for (int id2 = first; id2 <= last; id2++)
								{
									final StatsSet plr = _playerList.get(id2);

									packetWriter.writeString(plr.getString("name"));
									packetWriter.writeString(plr.getString("clanName"));
									packetWriter.writeD(plr.getInteger("level"));
									packetWriter.writeD(plr.getInteger("classId"));
									packetWriter.writeD(plr.getInteger("race"));
									packetWriter.writeD(id2); // server rank
									packetWriter.writeD(id2);
									packetWriter.writeD(id2);
									packetWriter.writeD(5316850);

									/*
									 * if (_snapshotList.size() > 0) { for (int id3 : _snapshotList.keySet()) {
									 * final StatsSet snapshot = _snapshotList.get(id3); if
									 * (player.getInteger("charId") == snapshot.getInteger("charId")) { packetWriter.writeD(id3);
									 * // server rank snapshot packetWriter.writeD(snapshot.getInteger("raceRank", 0));
									 * packetWriter.writeD(5316850); } } }
									 */
								}
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

						for (int i = 1; i <= _playerList.size(); i++)
						{
							final StatsSet player = _playerList.get(i);
							if (_race == player.getInteger("race"))
							{
								count++;
							}
						}
						packetWriter.writeD(count > 100 ? 100 : count);

						int i = 1;
						for (int id : _playerList.keySet())
						{
							final StatsSet player = _playerList.get(id);

							if (_race == player.getInteger("race"))
							{
								packetWriter.writeString(player.getString("name"));
								packetWriter.writeString(player.getString("clanName"));
								packetWriter.writeD(player.getInteger("level"));
								packetWriter.writeD(player.getInteger("classId"));
								packetWriter.writeD(player.getInteger("race"));
								packetWriter.writeD(i); // server rank
								if (_snapshotList.size() > 0)
								{
									final Map<Integer, StatsSet> snapshotRaceList = new ConcurrentHashMap<>();
									int j = 1;
									for (int id2 : _snapshotList.keySet())
									{
										final StatsSet snapshot = _snapshotList.get(id2);

										if (_race == snapshot.getInteger("race"))
										{
											snapshotRaceList.put(j, _snapshotList.get(id2));
											j++;
										}
									}
									for (int id2 : snapshotRaceList.keySet())
									{
										final StatsSet snapshot = snapshotRaceList.get(id2);

										if (player.getInteger("charId") == snapshot.getInteger("charId"))
										{
											packetWriter.writeD(id2); // server rank snapshot
											packetWriter.writeD(snapshot.getInteger("raceRank", 0)); // race rank snapshot
											packetWriter.writeD(5316850);
										}
									}
								}
								else
								{
									packetWriter.writeD(i);
									packetWriter.writeD(i);
									packetWriter.writeD(5316850);
								}
								i++;
							}
						}
					}
					else
					{
						boolean found = false;

						final Map<Integer, StatsSet> raceList = new ConcurrentHashMap<>();
						int i = 1;
						for (int id : _playerList.keySet())
						{
							final StatsSet set = _playerList.get(id);

							if (_player.getRace().ordinal() == set.getInteger("race"))
							{
								raceList.put(i, _playerList.get(id));
								i++;
							}
						}

						for (int id : raceList.keySet())
						{
							final StatsSet player = raceList.get(id);

							if (player.getInteger("charId") == _player.getObjectId())
							{
								found = true;
								final int first = id > 10 ? (id - 9) : 1;
								final int last = raceList.size() >= (id + 10) ? id + 10 : id + (raceList.size() - id);
								if (first == 1)
								{
									packetWriter.writeD(last - (first - 1));
								}
								else
								{
									packetWriter.writeD(last - first);
								}
								for (int id2 = first; id2 <= last; id2++)
								{
									final StatsSet plr = raceList.get(id2);

									packetWriter.writeString(plr.getString("name"));
									packetWriter.writeString(plr.getString("clanName"));
									packetWriter.writeD(plr.getInteger("level"));
									packetWriter.writeD(plr.getInteger("classId"));
									packetWriter.writeD(plr.getInteger("race"));
									packetWriter.writeD(id2); // server rank
									packetWriter.writeD(id2);
									packetWriter.writeD(id2);
									packetWriter.writeD(id2);
								}
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
						for (int id : _playerList.keySet())
						{
							final StatsSet set = _playerList.get(id);

							if (_player.getClan().getName() == set.getString("clanName"))
							{
								clanList.put(i, _playerList.get(id));
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
							packetWriter.writeD(player.getInteger("classId"));
							packetWriter.writeD(player.getInteger("race"));
							packetWriter.writeD(id); // clan rank
							if (_snapshotList.size() > 0)
							{
								for (int id2 : _snapshotList.keySet())
								{
									final StatsSet snapshot = _snapshotList.get(id2);

									if (player.getInteger("charId") == snapshot.getInteger("charId"))
									{
										packetWriter.writeD(id2); // server rank snapshot
										packetWriter.writeD(snapshot.getInteger("raceRank", 0)); // race rank snapshot
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
							for (Integer id2 : _playerList.keySet())
							{
								final StatsSet temp = _playerList.get(id2);
								if (temp.getInteger("charId") == friend.getObjectId())
								{
									friendList.add(temp.getInteger("charId"));
									count++;
								}
							}
						}
						friendList.add(_player.getObjectId());

						packetWriter.writeD(count);

						for (int id : _playerList.keySet())
						{
							final StatsSet player = _playerList.get(id);
							if (friendList.contains(player.getInteger("charId")))
							{
								packetWriter.writeString(player.getString("name"));
								packetWriter.writeString(player.getString("clanName"));
								packetWriter.writeD(player.getInteger("level"));
								packetWriter.writeD(player.getInteger("classId"));
								packetWriter.writeD(player.getInteger("race"));
								packetWriter.writeD(id); // friend rank
								if (_snapshotList.size() > 0)
								{
									for (int id2 : _snapshotList.keySet())
									{
										final StatsSet snapshot = _snapshotList.get(id2);

										if (player.getInteger("charId") == snapshot.getInteger("charId"))
										{
											packetWriter.writeD(id2); // server rank snapshot
											packetWriter.writeD(snapshot.getInteger("raceRank", 0)); // race rank snapshot
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

						packetWriter.writeString(_player.getName());
						if (_player.getClan() != null)
						{
							packetWriter.writeString(_player.getClan().getName());
						}
						else
						{
							packetWriter.writeString("");
						}
						packetWriter.writeD(_player.getBaseSubClass().getLevel()); // level
						packetWriter.writeD(_player.getBaseSubClass().getClassId());
						packetWriter.writeD(_player.getRace().ordinal());
						packetWriter.writeD(1); // clan rank
						if (_snapshotList.size() > 0)
						{
							for (Integer id : _snapshotList.keySet())
							{
								final StatsSet snapshot = _snapshotList.get(id);

								if (_player.getObjectId() == snapshot.getInteger("charId"))
								{
									packetWriter.writeD(id); // server rank snapshot
									packetWriter.writeD(snapshot.getInteger("raceRank", 0)); // race rank snapshot
									packetWriter.writeD(5316850);
								}
							}
						}
						else
						{
							packetWriter.writeD(0);
							packetWriter.writeD(0);
							packetWriter.writeD(5316850);
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