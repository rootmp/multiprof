package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 */
public class ExReplyRegisterDominion implements IClientOutgoingPacket
{
	public ExReplyRegisterDominion()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00);
		return true;
	}
}