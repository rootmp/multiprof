package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabNormalSlot implements IClientOutgoingPacket
{
	private int nBossID;
	private int nSlotID;
	private int nRemainCard;

	public ExAdenlabNormalSlot(int nBossID, int nSlotID, int nRemainCard)
	{
		this.nBossID = nBossID;
		this.nSlotID = nSlotID;
		this.nRemainCard = nRemainCard;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeD(nSlotID);
		packetWriter.writeD(nRemainCard);
		return true;
	}
}
