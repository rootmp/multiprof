package l2s.gameserver.network.l2.s2c.steadybox;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExSteadyBoxReward implements IClientOutgoingPacket
{
	private int _slotId, _itemId, _itemCount;

	public ExSteadyBoxReward(int slotId, int itemId, int itemCount)
	{
		_slotId = slotId;
		_itemId = itemId;
		_itemCount = itemCount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_slotId); // slot id
		packetWriter.writeD(_itemId); // item id
		packetWriter.writeD(_itemCount); // item count
		packetWriter.writeD(0); // ?
		packetWriter.writeD(0); // ?
		return true;
	}
}