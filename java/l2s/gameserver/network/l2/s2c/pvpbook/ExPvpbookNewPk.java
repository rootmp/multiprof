package l2s.gameserver.network.l2.s2c.pvpbook;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExPvpbookNewPk implements IClientOutgoingPacket
{
	private final String killerName;

	public ExPvpbookNewPk(String killerName)
	{
		this.killerName = killerName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeSizedString(killerName);
		return true;
	}
}
