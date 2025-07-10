package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExShowUsmPacket implements IClientOutgoingPacket
{
	private int _usmVideoId;

	public ExShowUsmPacket(int usmVideoId)
	{
		_usmVideoId = usmVideoId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_usmVideoId);
	}
}