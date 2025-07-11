package l2s.gameserver.network.l2.s2c;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.StatsSet;

/**
 * @author NviX
 */
public class ExOlympiadRankingInfo implements IClientOutgoingPacket
{
	private final Player _player;

	private final int _tabId;
	private final int _rankingType;
	private final int _unk;
	private final int _classId;
	private final int _serverId;
	private final Map<Integer, StatsSet> _playerList;
	private final Map<Integer, StatsSet> _snapshotList;

	public ExOlympiadRankingInfo(Player player, int tabId, int rankingType, int unk, int classId, int serverId)
	{
		_player = player;
		_tabId = tabId;
		_rankingType = rankingType;
		_unk = unk;
		_classId = classId;
		_serverId = serverId;
		_playerList = RankManager.getInstance().getOlyRankList();
		_snapshotList = RankManager.getInstance().getSnapshotOlyList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_tabId); // Tab id
		packetWriter.writeC(_rankingType); // ranking type
		packetWriter.writeC(_unk); // unk, shows 1 all time
		packetWriter.writeD(_classId); // class id (default 148) or caller class id for personal rank
		packetWriter.writeD(_serverId); // 0 - all servers, server id - for caller server
		packetWriter.writeD(933); // unk, 933 all time

		if (_playerList.size() > 0)
		{
			switch (_tabId)
			{
				case 0:
				{
					if (_rankingType == 0)
					{
						packetWriter.writeD(Math.min(_playerList.size(), 100));

						for (Integer id : _playerList.keySet())
						{
							final StatsSet player = _playerList.get(id);
							packetWriter.writeString(player.getString("name")); // name
							packetWriter.writeString(player.getString("clanName")); // clan name
							packetWriter.writeD(id); // rank

							if (_snapshotList.size() > 0)
							{
								for (Integer id2 : _snapshotList.keySet())
								{
									final StatsSet snapshot = _snapshotList.get(id2);
									if (player.getInteger("charId") == snapshot.getInteger("charId"))
									{
										packetWriter.writeD(id2); // previous rank
									}
								}
							}
							else
							{
								packetWriter.writeD(id);
							}

							packetWriter.writeD(Config.REQUEST_ID);// server id
							packetWriter.writeD(player.getInteger("level"));// level
							packetWriter.writeD(player.getInteger("classId"));// class id
							packetWriter.writeD(player.getInteger("clanLevel"));// clan level
							packetWriter.writeD(player.getInteger("competitions_win"));// win count
							packetWriter.writeD(player.getInteger("competitions_loose"));// lose count
							packetWriter.writeD(player.getInteger("olympiad_points"));// points
							packetWriter.writeD(player.getInteger("count"));// hero counts
							packetWriter.writeD(player.getInteger("legend_count"));// legend counts
						}
					}
					else
					{
						boolean found = false;
						for (Integer id : _playerList.keySet())
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
									packetWriter.writeD(id2);
									if (_snapshotList.size() > 0)
									{
										for (Integer id3 : _snapshotList.keySet())
										{
											final StatsSet snapshot = _snapshotList.get(id3);
											if (player.getInteger("charId") == snapshot.getInteger("charId"))
											{
												packetWriter.writeD(id3); // class rank snapshot
											}
										}
									}
									else
									{
										packetWriter.writeD(id2);
									}

									packetWriter.writeD(Config.REQUEST_ID);
									packetWriter.writeD(plr.getInteger("level"));
									packetWriter.writeD(plr.getInteger("classId"));
									packetWriter.writeD(plr.getInteger("clanLevel"));// clan level
									packetWriter.writeD(plr.getInteger("competitions_win"));// win count
									packetWriter.writeD(plr.getInteger("competitions_loose"));// lose count
									packetWriter.writeD(plr.getInteger("olympiad_points"));// points
									packetWriter.writeD(plr.getInteger("count"));// hero counts
									packetWriter.writeD(plr.getInteger("legend_count"));// legend counts
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
				case 1:
				{
					if (_rankingType == 0)
					{
						int count = 0;
						for (int i = 1; i <= _playerList.size(); i++)
						{
							final StatsSet player = _playerList.get(i);
							if (_classId == player.getInteger("classId"))
							{
								count++;
							}
						}
						packetWriter.writeD(count > 50 ? 50 : count);

						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatsSet player = _playerList.get(id);
							if (_classId == player.getInteger("classId"))
							{
								packetWriter.writeString(player.getString("name"));
								packetWriter.writeString(player.getString("clanName"));
								packetWriter.writeD(i); // class rank
								if (_snapshotList.size() > 0)
								{
									final Map<Integer, StatsSet> snapshotRaceList = new ConcurrentHashMap<>();
									int j = 1;
									for (Integer id2 : _snapshotList.keySet())
									{
										final StatsSet snapshot = _snapshotList.get(id2);
										if (_classId == snapshot.getInteger("classId"))
										{
											snapshotRaceList.put(j, _snapshotList.get(id2));
											j++;
										}
									}
									for (Integer id2 : snapshotRaceList.keySet())
									{
										final StatsSet snapshot = snapshotRaceList.get(id2);
										if (player.getInteger("charId") == snapshot.getInteger("charId"))
										{
											packetWriter.writeD(id2); // class rank snapshot
										}
									}
								}
								else
								{
									packetWriter.writeD(i);
								}

								packetWriter.writeD(Config.REQUEST_ID);
								packetWriter.writeD(player.getInteger("level"));
								packetWriter.writeD(player.getInteger("classId"));
								packetWriter.writeD(player.getInteger("clanLevel"));// clan level
								packetWriter.writeD(player.getInteger("competitions_win"));// win count
								packetWriter.writeD(player.getInteger("competitions_loose"));// lose count
								packetWriter.writeD(player.getInteger("olympiad_points"));// points
								packetWriter.writeD(player.getInteger("count"));// hero counts
								packetWriter.writeD(player.getInteger("legend_count"));// legend counts
								i++;
							}
						}
					}
					else
					{
						boolean found = false;
						final Map<Integer, StatsSet> classList = new ConcurrentHashMap<>();
						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatsSet set = _playerList.get(id);
							if (_player.getBaseClassId() == set.getInteger("classId"))
							{
								classList.put(i, _playerList.get(id));
								i++;
							}
						}

						for (Integer id : classList.keySet())
						{
							final StatsSet player = classList.get(id);
							if (player.getInteger("charId") == _player.getObjectId())
							{
								found = true;
								final int first = id > 10 ? (id - 9) : 1;
								final int last = classList.size() >= (id + 10) ? id + 10 : id + (classList.size() - id);
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
									final StatsSet plr = classList.get(id2);
									packetWriter.writeString(plr.getString("name"));
									packetWriter.writeString(plr.getString("clanName"));
									packetWriter.writeD(id2); // class rank
									packetWriter.writeD(id2);
									packetWriter.writeD(Config.REQUEST_ID);
									packetWriter.writeD(player.getInteger("level"));
									packetWriter.writeD(player.getInteger("classId"));
									packetWriter.writeD(player.getInteger("clanLevel"));// clan level
									packetWriter.writeD(player.getInteger("competitions_win"));// win count
									packetWriter.writeD(player.getInteger("competitions_loose"));// lose count
									packetWriter.writeD(player.getInteger("olympiad_points"));// points
									packetWriter.writeD(player.getInteger("count"));// hero counts
									packetWriter.writeD(player.getInteger("legend_count"));// legend counts
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
			}
		}
		return true;
	}
}
