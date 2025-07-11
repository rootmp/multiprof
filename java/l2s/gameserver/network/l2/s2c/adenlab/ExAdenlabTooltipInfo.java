package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.adenLab.PkAdenLabBossTooltip;

public class ExAdenlabTooltipInfo implements IClientOutgoingPacket
{
	private PkAdenLabBossTooltip[] bossTooltips;

	public ExAdenlabTooltipInfo(PkAdenLabBossTooltip[] bossTooltips)
	{
		this.bossTooltips = bossTooltips;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(bossTooltips.length);
		for(int i = 0; i < bossTooltips.length; i++)
		{
			packetWriter.writeD(bossTooltips[i].nBossID);
			packetWriter.writeD(bossTooltips[i].nTranscend);
		}
		return true;
	}
}
