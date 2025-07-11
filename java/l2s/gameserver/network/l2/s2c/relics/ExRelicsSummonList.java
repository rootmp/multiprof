package l2s.gameserver.network.l2.s2c.relics;

import java.util.Collection;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.RelicHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.relics.RelicsSummonInfo;

public class ExRelicsSummonList implements IClientOutgoingPacket
{
	private Collection<RelicsSummonInfo> infos;
	private Player player;

	public ExRelicsSummonList(Player player)
	{
		infos = RelicHolder.getInstance().getSummonInfos();
		this.player= player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(infos.size());
		for(RelicsSummonInfo info : infos)
		{
			packetWriter.writeD(info.getSummonId());
			packetWriter.writeD(info.getRemainTime());
			packetWriter.writeD(player.getVarInt("RelicsSummonLimit_"+info.getSummonId(), info.getDailyLimit()));
		}
		
		return true;
	}
}
