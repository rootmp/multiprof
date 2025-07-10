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
public class ExPledgeRankingList implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		int count = 0;
		packetWriter.writeC(_tabId); // top 150 or my clan rating

		if (_clanList.size() > 0)
		{
			switch (_tabId)
			{
				case 0: // all
				{
					count = _clanList.size() > 150 ? 150 : _clanList.size();
					packetWriter.writeD(count); // clans size

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

						packetWriter.writeD(i); // rank
						packetWriter.writeD(previousRank);
						writeString(clanName);
						packetWriter.writeD(clanLevel);
						writeString(leaderName);
						packetWriter.writeD(leaderLevel);
						packetWriter.writeD(clanMembers);
						packetWriter.writeD(points);

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
						packetWriter.writeD(0);
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
								packetWriter.writeD(last - (first - 1));
							else
								packetWriter.writeD(last - first);

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

									packetWriter.writeD(i); // rank
									packetWriter.writeD(previousRank);
									writeString(clanName);
									packetWriter.writeD(clanLevel);
									writeString(leaderName);
									packetWriter.writeD(leaderLevel);
									packetWriter.writeD(clanMembers);
									packetWriter.writeD(points);
								}
								i++;
							}
						}
						rank++;
					}
					if (!found)
						packetWriter.writeD(0);
					break;
				}
			}
		}
		else
		{
			packetWriter.writeD(count); // clans size
		}
	}
}