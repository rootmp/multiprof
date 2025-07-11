package l2s.gameserver.network.l2.s2c.pledge;

import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.ranking.PkPledgeRanking;

public class ExPledgeRankingMyInfo implements IClientOutgoingPacket
{
	private Player _player;
	private final Map<Integer, PkPledgeRanking> _clanList;
	private final Map<Integer, PkPledgeRanking> _previousClanList;

	public ExPledgeRankingMyInfo(Player player)
	{
		_player = player;
		_clanList = RankManager.getInstance().getClanRankList();
		_previousClanList = RankManager.getInstance().getPreviousClanRankList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if(_player.getClan() == null)
		{
			packetWriter.writeD(0); // rank
			packetWriter.writeD(0); // previous day rank
			packetWriter.writeD(0); // points
		}
		else
		{
			int clanId = _player.getClanId();
			int clanRank = 0;
			int clanPreviousRank = 0;
			int points = 0;
			if(_clanList.size() > 0)
			{
				int i = 1;
				for(int id : _clanList.keySet())
				{
					if(id == clanId)
					{
						clanRank = i;
						PkPledgeRanking ranking = _clanList.get(id);
						points = ranking.nPledgeExp;
						break;
					}
					i++;
				}
				if(_previousClanList.size() > 0)
				{
					i = 1;
					for(int id : _previousClanList.keySet())
					{
						if(id == clanId)
						{
							clanPreviousRank = i;
							break;
						}
						i++;
					}
				}
			}
			packetWriter.writeD(clanRank);
			packetWriter.writeD(clanPreviousRank);
			packetWriter.writeD(points);
		}
		return true;
	}
}
