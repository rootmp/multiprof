package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public final class ExEnchantFail implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExEnchantFail(0, 0);

	private final int _itemOne;
	private final int _itemTwo;

	public ExEnchantFail(int itemOne, int itemTwo)
	{
		_itemOne = itemOne;
		_itemTwo = itemTwo;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemOne);
		packetWriter.writeD(_itemTwo);
	}
}