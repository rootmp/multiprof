package l2s.gameserver.network.l2.s2c.pvpbook;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

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
		packetWriter.writeString(killerName);
	}
}
