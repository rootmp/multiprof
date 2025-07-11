package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExAutoplayDoMacro implements IClientOutgoingPacket
{
	public ExAutoplayDoMacro()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(20);
		packetWriter.writeC(1);
		packetWriter.writeH(0);
		return true;
	}
}
