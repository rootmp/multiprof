package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExSetPartyLooting implements IClientOutgoingPacket
{
	private int _result;
	private int _mode;

	public ExSetPartyLooting(int result, int mode)
	{
		_result = result;
		_mode = mode;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_result);
		packetWriter.writeD(_mode);
	}
}
