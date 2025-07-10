package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExAskModifyPartyLooting implements IClientOutgoingPacket
{
	private String _requestor;
	private int _mode;

	public ExAskModifyPartyLooting(String name, int mode)
	{
		_requestor = name;
		_mode = mode;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_requestor);
		packetWriter.writeD(_mode);
	}
}
