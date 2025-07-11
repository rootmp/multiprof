package l2s.gameserver.network.l2.s2c.pledge;

import java.util.Map;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.ranking.PkPledgeRanking;

public class ExPledgeRankingList implements IClientOutgoingPacket
{
	private Player _player;
	private int _tabId;
	private Map<Integer, PkPledgeRanking> _clanList;
	private Map<Integer, PkPledgeRanking> _previousClanList;

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

		if(_clanList.size() > 0)
		{
			switch(_tabId)
			{
				case 0: // all
				{
					count = _clanList.size() > 150 ? 150 : _clanList.size();
					packetWriter.writeD(count); // clans size

					int i = 1;
					for(int id : _clanList.keySet())
					{
						PkPledgeRanking ranking = _clanList.get(id);
						PkPledgeRanking previousRanking = _previousClanList.get(id);

						int previousRank = previousRanking != null ? previousRanking.nRank : 0;

						packetWriter.writeD(ranking.nRank); // rank
						packetWriter.writeD(previousRank);
						packetWriter.writeSizedString(ranking.sPledgeName);
						packetWriter.writeD(ranking.nPledgeLevel);
						packetWriter.writeSizedString(ranking.sPledgeMasterName);
						packetWriter.writeD(ranking.nPledgeMasterLevel);
						packetWriter.writeD(ranking.nPledgeMemberCount);
						packetWriter.writeD(ranking.nPledgeExp);

						if(i >= 150)
							break;
						else
							i++;
					}
					break;
				}
				case 1:
				{
					if(_player.getClan() == null)
					{
						packetWriter.writeD(0);
						break;
					}

					boolean found = false;
					int rank = 1;
					for(int id : _clanList.keySet())
					{
						if(_player.getClan().getClanId() == id)
						{
							found = true;
							int first = rank > 10 ? (rank - 9) : 1;
							int last = _clanList.size() >= (rank + 10) ? rank + 10 : rank + (_clanList.size() - rank);

							if(first == 1)
								packetWriter.writeD(last - (first - 1));
							else
								packetWriter.writeD(last - first);

							int i = 1;
							for(int id2 : _clanList.keySet())
							{
								if((i >= first) && (i <= last))
								{
									PkPledgeRanking ranking = _clanList.get(id2);
									PkPledgeRanking previousRanking = _previousClanList.get(id2);

									int previousRank = previousRanking != null ? previousRanking.nRank : 0;

									packetWriter.writeD(ranking.nRank); // rank
									packetWriter.writeD(previousRank);
									packetWriter.writeSizedString(ranking.sPledgeName);
									packetWriter.writeD(ranking.nPledgeLevel);
									packetWriter.writeSizedString(ranking.sPledgeMasterName);
									packetWriter.writeD(ranking.nPledgeMasterLevel);
									packetWriter.writeD(ranking.nPledgeMemberCount);
									packetWriter.writeD(ranking.nPledgeExp);
								}
								i++;
							}
						}
						rank++;
					}
					if(!found)
						packetWriter.writeD(0);
					break;
				}
				default:
					break;
			}
		}
		else
		{
			packetWriter.writeD(count); // clans size
		}

		return true;
	}
}
