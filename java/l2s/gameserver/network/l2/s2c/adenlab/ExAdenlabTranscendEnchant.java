package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabTranscendEnchant implements IClientOutgoingPacket
{
	private int nBossID;
	private boolean cSuccess;

	public ExAdenlabTranscendEnchant(int nBossID, boolean cSuccess)
	{
		this.nBossID = nBossID;
		this.cSuccess = cSuccess;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeC(cSuccess ? 1 : 0);
		return true;
	}
}
