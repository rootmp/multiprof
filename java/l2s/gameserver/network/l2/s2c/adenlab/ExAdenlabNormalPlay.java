package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabNormalPlay implements IClientOutgoingPacket
{
	private int nBossID;
	private int nSlotID;
	private boolean cSuccess;

	public ExAdenlabNormalPlay(int nBossID, int nSlotID, boolean cSuccess)
	{
		this.nBossID = nBossID;
		this.nSlotID = nSlotID;
		this.cSuccess = cSuccess;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeD(nSlotID);
		packetWriter.writeC(cSuccess);
		return true;
	}
}
