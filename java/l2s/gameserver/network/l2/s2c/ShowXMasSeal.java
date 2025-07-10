package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ShowXMasSeal implements IClientOutgoingPacket
{
	private int _item;

	public ShowXMasSeal(int item)
	{
		_item = item;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_item);
	}
}