package l2s.gameserver.network.l2.s2c.items.autopeel;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExStopItemAutoPeel extends L2GameServerPacket
{
	private static int _result;

	public ExStopItemAutoPeel(int result)
	{
		_result = result;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(_result); // result
	}
}