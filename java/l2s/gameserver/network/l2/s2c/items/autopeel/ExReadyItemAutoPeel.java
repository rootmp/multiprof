package l2s.gameserver.network.l2.s2c.items.autopeel;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExReadyItemAutoPeel extends L2GameServerPacket
{
	private static int _result;
	private static int _itemObjId;

	public ExReadyItemAutoPeel(int result, int itemObjId)
	{
		_result = result;
		_itemObjId = itemObjId;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(_result);
		writeD(_itemObjId);
	}
}