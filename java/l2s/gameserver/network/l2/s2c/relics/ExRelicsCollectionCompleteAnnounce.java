package l2s.gameserver.network.l2.s2c.relics;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRelicsCollectionCompleteAnnounce implements IClientOutgoingPacket
{
	private int nCollectionID;

	public ExRelicsCollectionCompleteAnnounce(int nCollectionID)
	{
		this.nCollectionID = nCollectionID;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nCollectionID);
		return true;
	}
}