package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * format: cS
 */
public class FriendAddRequest implements IClientOutgoingPacket
{
	private String _requestorName;

	public FriendAddRequest(String requestorName)
	{
		_requestorName = requestorName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0); // 0
		packetWriter.writeS(_requestorName);
	}
}