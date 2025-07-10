package l2s.gameserver.network.l2.s2c.itemrestore;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExItemRestore extends L2GameServerPacket
{
	public static ExItemRestore FAIL = new ExItemRestore(0);
	public static ExItemRestore SUCCESS = new ExItemRestore(1);

	private final int cResult;

	private ExItemRestore(int cResult)
	{
		this.cResult = cResult;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(cResult);
	}
}