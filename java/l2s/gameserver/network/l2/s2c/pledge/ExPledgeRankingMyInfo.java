package l2s.gameserver.network.l2.s2c.pledge;

import java.util.Map;

import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPledgeRankingMyInfo extends L2GameServerPacket
{
	private Player _player;
	private final Map<Integer, Integer> _clanList;
	private final Map<Integer, Integer> _previousClanList;

	public ExPledgeRankingMyInfo(Player player)
	{
		_player = player;
		_clanList = RankManager.getInstance().getClanRankList();
		_previousClanList = RankManager.getInstance().getPreviousClanRankList();
	}

	@Override
	protected final void writeImpl()
	{
		if (_player.getClan() == null)
		{
			writeD(0); // rank
			writeD(0); // previous day rank
			writeD(0); // points
		}
		else
		{
			int clanId = _player.getClanId();
			int clanRank = 0;
			int clanPreviousRank = 0;
			int points = 0;
			if (_clanList.size() > 0)
			{
				int i = 1;
				for (int id : _clanList.keySet())
				{
					if (id == clanId)
					{
						clanRank = i;
						points = _clanList.get(id);
						break;
					}
					i++;
				}
				if (_previousClanList.size() > 0)
				{
					i = 1;
					for (int id : _previousClanList.keySet())
					{
						if (id == clanId)
						{
							clanPreviousRank = i;
							break;
						}
						i++;
					}
				}
			}
			writeD(clanRank);
			writeD(clanPreviousRank);
			writeD(points);
		}
	}
}