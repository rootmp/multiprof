package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExPrivateStoreBuyingResult implements IClientOutgoingPacket
{
	private final int _itemObjId;
	private final long _itemCount;
	private final String _sellerName;

	public ExPrivateStoreBuyingResult(int itemObjId, long itemCount, String sellerName)
	{
		_itemObjId = itemObjId;
		_itemCount = itemCount;
		_sellerName = sellerName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemObjId);
		packetWriter.writeQ(_itemCount);
		packetWriter.writeS(_sellerName);
	}
}