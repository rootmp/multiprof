package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class ExPeriodicHenna implements IClientOutgoingPacket
{

	public ExPeriodicHenna(Player player)
	{

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00); // Premium symbol ID
		packetWriter.writeD(0x00); // Premium symbol left time
		packetWriter.writeD(0x00); // Premium symbol active
		return true;
	}
}
