package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExReplyDominionInfo implements IClientOutgoingPacket
{
	public ExReplyDominionInfo()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00);
		return true;
	}
}