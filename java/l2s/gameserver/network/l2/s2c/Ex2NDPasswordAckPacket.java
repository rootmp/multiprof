package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class Ex2NDPasswordAckPacket implements IClientOutgoingPacket
{
	public static final int SUCCESS = 0x00;
	public static final int WRONG_PATTERN = 0x01;

	private int _response;

	public Ex2NDPasswordAckPacket(int response)
	{
		_response = response;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0x00);
		packetWriter.writeD(_response == WRONG_PATTERN ? 0x01 : 0x00);
		packetWriter.writeD(0x00);
	}
}