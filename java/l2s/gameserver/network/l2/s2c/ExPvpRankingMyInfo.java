package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.ranking.PVPRankingRankInfo;

public class ExPvpRankingMyInfo implements IClientOutgoingPacket
{
	private final PVPRankingRankInfo _data;

	public ExPvpRankingMyInfo(Player player)
	{
		_data = RankManager.getInstance().getPvpRankData(player);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_data.nPVPPoint); // pvp points
		packetWriter.writeD(_data.nRank); // current rank
		packetWriter.writeD(_data.nPrevRank); // ingame shown change in rank as this value - current rank value.
		packetWriter.writeD(_data.nKillCount); // kills
		packetWriter.writeD(_data.nDieCount); // deaths

		return true;
	}
}