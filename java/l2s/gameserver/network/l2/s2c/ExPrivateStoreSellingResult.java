package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExPrivateStoreSellingResult implements IClientOutgoingPacket
{
	private final int _itemObjId;
	private final long _itemCount;
	private final String _buyerName;

	public ExPrivateStoreSellingResult(int itemObjId, long itemCount, String buyerName)
	{
		_itemObjId = itemObjId;
		_itemCount = itemCount;
		_buyerName = buyerName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemObjId);
		packetWriter.writeQ(_itemCount);
		packetWriter.writeS(_buyerName);
	}
}