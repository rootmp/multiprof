package l2s.gameserver.network.l2.s2c.steadybox;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExSteadyBoxReward extends L2GameServerPacket
{
	private int _slotId, _itemId, _itemCount;

	public ExSteadyBoxReward(int slotId, int itemId, int itemCount)
	{
		_slotId = slotId;
		_itemId = itemId;
		_itemCount = itemCount;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_slotId); // slot id
		writeD(_itemId); // item id
		writeD(_itemCount); // item count
		writeD(0); // ?
		writeD(0); // ?
	}
}