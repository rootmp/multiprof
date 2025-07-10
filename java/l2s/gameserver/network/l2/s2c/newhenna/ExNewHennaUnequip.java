package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExNewHennaUnequip extends L2GameServerPacket
{
	private final int cSlotID;
	private final boolean cSuccess;

	public ExNewHennaUnequip(int cSlotID, boolean cSuccess)
	{
		this.cSlotID = cSlotID;
		this.cSuccess = cSuccess;
	}

	@Override
	protected void writeImpl()
	{
		writeC(cSlotID);
		writeC(cSuccess);
	}
}
