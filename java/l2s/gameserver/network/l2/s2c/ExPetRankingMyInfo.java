package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class ExPetRankingMyInfo implements IClientOutgoingPacket
{
	@SuppressWarnings("unused")
	private final Player _player;

	public ExPetRankingMyInfo(Player player)
	{
		_player = player;

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		/*
		packetWriter.writeSizedString(sNickName);
		packetWriter.writeD(nCollarID);
		packetWriter.writeD(nNPCClassID);
		packetWriter.writeH(nIndex);
		packetWriter.writeH(nLevel);
		packetWriter.writeD(nRank);
		packetWriter.writeD(nPrevRank); 
		packetWriter.writeD(nRaceRank);
		packetWriter.writeD(nPrevRaceRank);
		*/

		packetWriter.writeSizedString("");
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		packetWriter.writeD(-1);
		packetWriter.writeH(0);
		packetWriter.writeH(0);
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		return true;
	}
}