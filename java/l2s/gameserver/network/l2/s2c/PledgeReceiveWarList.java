package l2s.gameserver.network.l2.s2c;

import java.util.Collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.ClanWar;

/**
 * @author GodWorld
 * @reworked by Bonux
 **/
public class PledgeReceiveWarList implements IClientOutgoingPacket
{
	private Clan _clan;
	private int _page;

	public PledgeReceiveWarList(Clan clan, int page)
	{
		_clan = clan;
		_page = page;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_page);

		Collection<ClanWar> wars = _clan.getWars().valueCollection();

		packetWriter.writeD(wars.size());
		for(ClanWar war : wars)
		{
			packetWriter.writeS(war.getOpposingClan(_clan).getName());
			packetWriter.writeD(war.getClanWarState(_clan).ordinal());
			packetWriter.writeD(0);
			packetWriter.writeD(war.getPointDiff(_clan));
			packetWriter.writeD(war.calculateWarProgress(_clan).ordinal());
			packetWriter.writeD(0); // Friends to start war left
		}
		return true;
	}
}