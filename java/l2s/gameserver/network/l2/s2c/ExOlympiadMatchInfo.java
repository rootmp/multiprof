package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExOlympiadMatchInfo implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS("Team 1");
		packetWriter.writeD(100);
		packetWriter.writeS("Team 2");
		packetWriter.writeD(100);
		packetWriter.writeD(1);
		packetWriter.writeD(3600);
	}
}