package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExTryMixCube implements IClientOutgoingPacket
{
	public static final L2GameServerPacket FAIL = new ExTryMixCube(6);

	private final int _result;
	private final int _itemId;
	private final long _itemCount;

	public ExTryMixCube(int result)
	{
		_result = result;
		_itemId = 0;
		_itemCount = 0;
	}

	public ExTryMixCube(int itemId, long itemCount)
	{
		_result = 0;
		_itemId = itemId;
		_itemCount = itemCount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_result);
		packetWriter.writeD(0x01); // UNK
		// for(int i = 0; i < count; i++)
		// {
		packetWriter.writeC(0x00);
		packetWriter.writeD(_itemId);
		packetWriter.writeQ(_itemCount);
		// }
		return true;
	}
}