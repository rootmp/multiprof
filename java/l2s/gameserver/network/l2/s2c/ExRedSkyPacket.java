package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExRedSkyPacket implements IClientOutgoingPacket
{
	private int _duration;

	public ExRedSkyPacket(int duration)
	{
		_duration = duration;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_duration);
	}
}