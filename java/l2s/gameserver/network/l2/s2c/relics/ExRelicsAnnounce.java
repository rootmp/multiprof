package l2s.gameserver.network.l2.s2c.relics;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRelicsAnnounce implements IClientOutgoingPacket
{
	private String sUserName;
	private int nRelicsID;

	public ExRelicsAnnounce(Player owner, int relicId)
	{
		sUserName = owner.getName();
		nRelicsID = relicId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeSizedString(sUserName);
		packetWriter.writeD(nRelicsID);
		return true;
	}
}