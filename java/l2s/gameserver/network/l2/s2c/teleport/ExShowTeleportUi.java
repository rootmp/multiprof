package l2s.gameserver.network.l2.s2c.teleport;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

public class ExShowTeleportUi implements IClientOutgoingPacket
{
	public static final ExShowTeleportUi STATIC = new ExShowTeleportUi();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}
