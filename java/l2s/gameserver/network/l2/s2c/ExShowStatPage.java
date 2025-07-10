package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public final class ExShowStatPage implements IClientOutgoingPacket
{
	private final int _page;

	public ExShowStatPage(int page)
	{
		_page = page;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_page);
	}
}