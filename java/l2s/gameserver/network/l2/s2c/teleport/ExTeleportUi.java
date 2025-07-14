package l2s.gameserver.network.l2.s2c.teleport;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExTeleportUi implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0);//скидка %
		return true;
	}
}