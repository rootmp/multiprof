package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class TradeRequestPacket implements IClientOutgoingPacket
{
	private int _senderId;

	public TradeRequestPacket(int senderId)
	{
		_senderId = senderId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_senderId);
		return true;
	}
}