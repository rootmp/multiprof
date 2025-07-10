package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class RestartResponsePacket implements IClientOutgoingPacket
{
	public static final RestartResponsePacket OK = new RestartResponsePacket(1), FAIL = new RestartResponsePacket(0);
	private String _message;
	private int _param;

	public RestartResponsePacket(int param)
	{
		_message = "bye";
		_param = param;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_param); // 01-ok
		packetWriter.writeS(_message);
	}
}