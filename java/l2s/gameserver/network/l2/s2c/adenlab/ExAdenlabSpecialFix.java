package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabSpecialFix implements IClientOutgoingPacket
{
	private int nBossID;
	private int nSlotID;
	private boolean bSuccess;

	public ExAdenlabSpecialFix(int nBossID, int nSlotID, boolean bSuccess)
	{
		this.nBossID = nBossID;
		this.nSlotID = nSlotID;
		this.bSuccess = bSuccess;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeD(nSlotID);
		packetWriter.writeC(bSuccess ? 1 : 0);
		return true;
	}
}
