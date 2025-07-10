package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public final class ExEnchantSucess implements IClientOutgoingPacket
{
	private final int _itemId;

	public ExEnchantSucess(int itemId)
	{
		_itemId = itemId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemId);
	}
}