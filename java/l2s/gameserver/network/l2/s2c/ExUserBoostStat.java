package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExUserBoostStat implements IClientOutgoingPacket
{
	private int _type;
	private int _count;
	private int _bonus;

	public ExUserBoostStat(int type, int count, int bonus)
	{
		_type = type;
		_count = count;
		_bonus = bonus;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_type);
		packetWriter.writeC(_count);
		packetWriter.writeH(_bonus);
	}
}