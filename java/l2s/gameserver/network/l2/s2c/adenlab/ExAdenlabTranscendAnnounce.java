package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabTranscendAnnounce implements IClientOutgoingPacket
{
	private String sUserName;
	private int nBossID;
	private int cEnchantCount;

	public ExAdenlabTranscendAnnounce(String sUserName, int nBossID, int cEnchantCount)
	{
		this.sUserName = sUserName;
		this.nBossID = nBossID;
		this.cEnchantCount = cEnchantCount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeSizedString(sUserName);
		packetWriter.writeD(nBossID);
		packetWriter.writeC(cEnchantCount);
		return true;
	}
}
