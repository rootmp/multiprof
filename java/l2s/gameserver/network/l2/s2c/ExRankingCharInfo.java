package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.ranking.PkRankerData;

public class ExRankingCharInfo implements IClientOutgoingPacket
{
	private final PkRankerData _playerData;
	private final PkRankerData _snapshotData;

	public ExRankingCharInfo(Player player)
	{
		_playerData = RankManager.getInstance().getRankData(player);
		_snapshotData = RankManager.getInstance().getSnapshotData(player);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_playerData.nServerRank); // server rank
		packetWriter.writeD(_playerData.nRaceRank); // race rank
		packetWriter.writeD(_snapshotData.nServerRank); // server rank snapshot
		packetWriter.writeD(_snapshotData.nRaceRank); // race rank snapshot
		packetWriter.writeD(_playerData.nClassRank); // class rank
		packetWriter.writeD(_snapshotData.nClassRank); // class rank snapshot
		return true;
	}
}