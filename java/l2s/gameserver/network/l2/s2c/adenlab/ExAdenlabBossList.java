package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAdenlabBossList implements IClientOutgoingPacket
{
	private int[] bossList;

	public ExAdenlabBossList(int[] bossList)
	{
		this.bossList = bossList;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(bossList.length);
		for(int bossID : bossList)
		{
			packetWriter.writeD(bossID);
		}
		return true;
	}
}
