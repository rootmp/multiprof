package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabUnlockBoss implements IClientOutgoingPacket
{
	private int nBossID;
	private boolean bSuccess;

	public ExAdenlabUnlockBoss(int nBossID, boolean bSuccess)
	{
		this.nBossID = nBossID;
		this.bSuccess = bSuccess;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeC(bSuccess);
		return true;
	}
}
