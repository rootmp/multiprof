package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class TutorialCloseHtmlPacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new TutorialCloseHtmlPacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}