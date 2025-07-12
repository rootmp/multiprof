package l2s.gameserver.network.l2.s2c.RaidAuction;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.item.PkRaidAuctionDropEffect;

public class ExRaidAuctionDropEffect implements IClientOutgoingPacket
{
	private List<PkRaidAuctionDropEffect> _dropEffects;

	public ExRaidAuctionDropEffect(List<PkRaidAuctionDropEffect> dropEffects)
	{
		_dropEffects = dropEffects;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_dropEffects.size());
		for(PkRaidAuctionDropEffect drop : _dropEffects)
		{
			packetWriter.writeD(drop.nClassID);
			packetWriter.writeD(drop.X);
			packetWriter.writeD(drop.Y);
			packetWriter.writeD(drop.Z);
		}
		return true;
	}
}
