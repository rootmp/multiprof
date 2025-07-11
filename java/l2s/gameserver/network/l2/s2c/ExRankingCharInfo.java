package l2s.gameserver.network.l2.s2c;

import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class ExRankingCharInfo implements IClientOutgoingPacket
{
	private final Player _player;
	private final Map<Integer, StatsSet> _playerList;
	private final Map<Integer, StatsSet> _snapshotList;

	public ExRankingCharInfo(Player player)
	{
		_player = player;
		_playerList = RankManager.getInstance().getRankList();
		_snapshotList = RankManager.getInstance().getSnapshotList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (_playerList.size() > 0)
		{
			for (Integer id : _playerList.keySet())
			{
				final StatsSet player = _playerList.get(id);
				if (player.getInteger("charId") == _player.getObjectId())
				{
					packetWriter.writeD(id); // server rank
					packetWriter.writeD(player.getInteger("raceRank")); // race rank

					for (Integer id2 : _snapshotList.keySet())
					{
						final StatsSet snapshot = _snapshotList.get(id2);
						if (player.getInteger("charId") == snapshot.getInteger("charId"))
						{
							packetWriter.writeD(id2); // server rank snapshot
							packetWriter.writeD(snapshot.getInteger("raceRank")); // race rank snapshot
							packetWriter.writeD(0); // class rank
							packetWriter.writeD(0); // class rank snapshot
							return true;
						}
					}
				}
			}
			packetWriter.writeD(0); // server rank
			packetWriter.writeD(0); // race rank
			packetWriter.writeD(0); // server rank snapshot
			packetWriter.writeD(0); // race rank snapshot
			packetWriter.writeD(0); // class rank
			packetWriter.writeD(0); // class rank snapshot
		}
		else
		{
			packetWriter.writeD(0); // server rank
			packetWriter.writeD(0); // race rank
			packetWriter.writeD(0); // server rank snapshot
			packetWriter.writeD(0); // race rank snapshot
			packetWriter.writeD(0); // class rank
			packetWriter.writeD(0); // class rank snapshot
		}
		return true;
	}
}