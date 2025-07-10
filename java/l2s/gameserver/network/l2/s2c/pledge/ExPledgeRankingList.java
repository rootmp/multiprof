package l2s.gameserver.network.l2.s2c.pledge;

import java.util.Map;

import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.tables.ClanTable;

/**
 * @author nexvill
 */
public class ExPledgeRankingList extends L2GameServerPacket
{
	private Player _player;
	private int _tabId;
	private Map<Integer, Integer> _clanList;
	private Map<Integer, Integer> _previousClanList;

	public ExPledgeRankingList(Player player, int tabId)
	{
		_player = player;
		_tabId = tabId;
		_clanList = RankManager.getInstance().getClanRankList();
		_previousClanList = RankManager.getInstance().getPreviousClanRankList();
	}

	@Override
	protected final void writeImpl()
	{
		int count = 0;
		writeC(_tabId); // top 150 or my clan rating

		if (_clanList.size() > 0)
		{
			switch (_tabId)
			{
				case 0: // all
				{
					count = _clanList.size() > 150 ? 150 : _clanList.size();
					writeD(count); // clans size

					int i = 1;
					for (int id : _clanList.keySet())
					{
						int clanId = id;
						int previousRank = 0;
						int j = 1;

						for (int id2 : _previousClanList.keySet())
						{
							if (id == id2)
							{
								previousRank = j;
								break;
							}
							j++;
						}
						Clan clan = ClanTable.getInstance().getClan(clanId);

						String clanName = clan.getName();
						int clanLevel = clan.getLevel();
						String leaderName = clan.getLeaderName();
						int leaderLevel = clan.getLeader().getLevel();
						int clanMembers = clan.getAllMembers().size();
						int points = clan.getPoints();

						writeD(i); // rank
						writeD(previousRank);
						writeString(clanName);
						writeD(clanLevel);
						writeString(leaderName);
						writeD(leaderLevel);
						writeD(clanMembers);
						writeD(points);

						if (i >= 150)
							break;
						else
							i++;
					}
					break;
				}
				case 1:
				{
					if (_player.getClan() == null)
					{
						writeD(0);
						break;
					}

					boolean found = false;
					int rank = 1;
					for (int id : _clanList.keySet())
					{
						if (_player.getClan().getClanId() == id)
						{
							found = true;
							int first = rank > 10 ? (rank - 9) : 1;
							int last = _clanList.size() >= (rank + 10) ? rank + 10 : rank + (_clanList.size() - rank);

							if (first == 1)
								writeD(last - (first - 1));
							else
								writeD(last - first);

							int i = 1;
							for (int id2 : _clanList.keySet())
							{
								if ((i >= first) && (i <= last))
								{
									int clanId = id2;
									int previousRank = 0;
									int j = 1;

									for (int id3 : _previousClanList.keySet())
									{
										if (id2 == id3)
										{
											previousRank = j;
											break;
										}
										j++;
									}

									Clan clan = ClanTable.getInstance().getClan(clanId);
									String clanName = clan.getName();
									int clanLevel = clan.getLevel();
									String leaderName = clan.getLeaderName();
									int leaderLevel = clan.getLeader().getLevel();
									int clanMembers = clan.getAllMembers().size();
									int points = clan.getPoints();

									writeD(i); // rank
									writeD(previousRank);
									writeString(clanName);
									writeD(clanLevel);
									writeString(leaderName);
									writeD(leaderLevel);
									writeD(clanMembers);
									writeD(points);
								}
								i++;
							}
						}
						rank++;
					}
					if (!found)
						writeD(0);
					break;
				}
			}
		}
		else
		{
			writeD(count); // clans size
		}
	}
}