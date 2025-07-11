package l2s.gameserver.network.l2.s2c;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.entity.events.objects.SiegeClanObject;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.model.pledge.Clan;

public class ExMercenaryCastlewarCastleSiegeAttackerList implements IClientOutgoingPacket
{
	private final int _id;
	private final boolean _registrationValid;
	private List<SiegeClanObject> _clans;

	public ExMercenaryCastlewarCastleSiegeAttackerList(Castle castle)
	{
		_id = castle.getId();

		CastleSiegeEvent siegeEvent = castle.getSiegeEvent();
		if (siegeEvent != null)
		{
			_registrationValid = !siegeEvent.isRegistrationOver();
			_clans = siegeEvent.getObjects(SiegeEvent.ATTACKERS);
		}
		else
		{
			_registrationValid = false;
			_clans = Collections.emptyList();
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// dddddd(dSSdddQddSSd)
		packetWriter.writeD(_id);
		packetWriter.writeD(0x00); // Owner's view
		packetWriter.writeD(_registrationValid);
		packetWriter.writeD(0x00); // Page number
		packetWriter.writeD(_clans.size());
		packetWriter.writeD(_clans.size());
		for (SiegeClanObject siegeClan : _clans)
		{
			Clan clan = siegeClan.getClan();
			packetWriter.writeD(clan.getClanId());
			packetWriter.writeS(clan.getName());
			packetWriter.writeS(clan.getLeaderName());
			packetWriter.writeD(clan.getCrestId());
			packetWriter.writeD((int) (siegeClan.getDate() / 1000L));
			packetWriter.writeD(siegeClan.getParam() > 0); // Have mercenaries recruting
			packetWriter.writeQ(siegeClan.getParam()); // Rawarding Rate %
			packetWriter.writeD(0x00); // Mercenaries count

			Alliance alliance = clan.getAlliance();
			if (alliance != null)
			{
				packetWriter.writeD(alliance.getAllyId());
				packetWriter.writeS(alliance.getAllyName());
				packetWriter.writeS(alliance.getAllyLeaderName());
				packetWriter.writeD(alliance.getAllyCrestId());
			}
			else
			{
				packetWriter.writeD(0x00);
				packetWriter.writeS(StringUtils.EMPTY);
				packetWriter.writeS(StringUtils.EMPTY);
				packetWriter.writeD(0x00);
			}
		}
		return true;
	}
}