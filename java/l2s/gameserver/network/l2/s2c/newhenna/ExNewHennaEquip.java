package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaEquip extends L2GameServerPacket
{
	private final int cSlotID;
	private final int nHennaID;
	private final boolean cSuccess;

	public ExNewHennaEquip(int cSlotID, int nHennaID)
	{
		this.cSlotID = cSlotID;
		this.nHennaID = nHennaID;
		this.cSuccess = true;
	}

	public ExNewHennaEquip(int cSlotID)
	{
		this.cSlotID = cSlotID;
		this.nHennaID = 0;
		this.cSuccess = false;
	}

	@Override
	protected void writeImpl()
	{
		writeC(cSlotID);
		writeD(nHennaID);
		writeC(cSuccess);
	}
}
