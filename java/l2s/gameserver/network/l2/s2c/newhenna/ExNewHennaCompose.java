package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaCompose extends L2GameServerPacket
{
	private final int nResultHennaID;
	private final int nResultItemID;
	private final boolean cSuccess;

	public ExNewHennaCompose(int nResultHennaID, int nResultItemID, boolean cSuccess)
	{
		this.nResultHennaID = nResultHennaID;
		this.nResultItemID = nResultItemID;
		this.cSuccess = cSuccess;
	}

	@Override
	protected void writeImpl()
	{
		writeD(nResultHennaID);
		writeD(nResultItemID);
		writeC(cSuccess);
	}
}
