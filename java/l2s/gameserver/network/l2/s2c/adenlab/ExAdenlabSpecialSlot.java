package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabSpecialSlot implements IClientOutgoingPacket
{
	private int nBossID;
	private int nSlotID;
	private int[] drawnOptionGrades;
	private int[] fixedOptionGrades;

	public ExAdenlabSpecialSlot(int nBossID, int nSlotID, int[] drawnOptionGrades, int[] fixedOptionGrades)
	{
		this.nBossID = nBossID;
		this.nSlotID = nSlotID;
		this.drawnOptionGrades = drawnOptionGrades;
		this.fixedOptionGrades = fixedOptionGrades;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeD(nSlotID);

		packetWriter.writeD(drawnOptionGrades.length);
		for(int option : drawnOptionGrades)
		{
			packetWriter.writeD(option);
		}

		packetWriter.writeD(fixedOptionGrades.length);
		for(int option : fixedOptionGrades)
		{
			packetWriter.writeD(option);
		}
		return true;
	}
}
