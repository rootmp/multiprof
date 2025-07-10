package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExCuriousHouseRemainTime implements IClientOutgoingPacket
{
	private int _time;

	public ExCuriousHouseRemainTime(int time)
	{
		_time = time;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_time);
	}
}
