package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class TradeDonePacket implements IClientOutgoingPacket
{
	public static final L2GameServerPacket SUCCESS = new TradeDonePacket(1);
	public static final L2GameServerPacket FAIL = new TradeDonePacket(0);

	private int _response;

	private TradeDonePacket(int num)
	{
		_response = num;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_response);
		return true;
	}
}