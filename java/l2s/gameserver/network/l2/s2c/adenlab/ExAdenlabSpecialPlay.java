package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabSpecialPlay implements IClientOutgoingPacket
{
	private int nBossID;
	private int nSlotID;
	private boolean bSuccess;
	private int[] drawnOptionGrades;

	public ExAdenlabSpecialPlay(int nBossID, int nSlotID, boolean bSuccess, int[] drawnOptionGrades)
	{
		this.nBossID = nBossID;
		this.nSlotID = nSlotID;
		this.bSuccess = bSuccess;
		this.drawnOptionGrades = drawnOptionGrades;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeD(nSlotID);
		packetWriter.writeC(bSuccess ? 1 : 0);
		packetWriter.writeD(drawnOptionGrades.length);
		for(int option : drawnOptionGrades)
		{
			packetWriter.writeD(option);
		}
		return true;
	}
}
