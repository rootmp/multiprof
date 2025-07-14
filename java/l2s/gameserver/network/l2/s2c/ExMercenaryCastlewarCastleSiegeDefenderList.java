package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
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

public class ExMercenaryCastlewarCastleSiegeDefenderList implements IClientOutgoingPacket
{
	private static class DefenderClan
	{
		private final Clan _clan;
		private final int _type;
		private final int _time;
		private final long _mercenaryReward;
		private final int _mercenariesCount;

		public DefenderClan(Clan clan, int type, int time, long mercenaryReward, int mercenariesCount)
		{
			_clan = clan;
			_type = type;
			_time = time;
			_mercenaryReward = mercenaryReward;
			_mercenariesCount = mercenariesCount;
		}
	}

	public static int OWNER = 1;
	public static int WAITING = 2;
	public static int ACCEPTED = 3;
	public static int REFUSE = 4;

	private final int _id;
	private final boolean _registrationValid;
	private final List<DefenderClan> _defenderClans;

	public ExMercenaryCastlewarCastleSiegeDefenderList(Castle castle)
	{
		_id = castle.getId();

		CastleSiegeEvent siegeEvent = castle.getSiegeEvent();
		if(siegeEvent != null)
		{
			_registrationValid = !siegeEvent.isRegistrationOver() && (castle.getOwner() != null);

			List<SiegeClanObject> defenders = siegeEvent.getObjects(SiegeEvent.DEFENDERS);
			List<SiegeClanObject> defendersWaiting = siegeEvent.getObjects(CastleSiegeEvent.DEFENDERS_WAITING);
			List<SiegeClanObject> defendersRefused = siegeEvent.getObjects(CastleSiegeEvent.DEFENDERS_REFUSED);
			_defenderClans = new ArrayList<>(defenders.size() + defendersWaiting.size() + defendersRefused.size());
			for(SiegeClanObject siegeClan : defenders)
			{
				_defenderClans.add(new DefenderClan(siegeClan.getClan(), siegeClan.getClan()
						== castle.getOwner() ? OWNER : ACCEPTED, (int) (siegeClan.getDate() / 1000L), siegeClan.getParam(), 0));
			}
			for(SiegeClanObject siegeClan : defendersWaiting)
			{
				_defenderClans.add(new DefenderClan(siegeClan.getClan(), WAITING, (int) (siegeClan.getDate() / 1000L), siegeClan.getParam(), 0));
			}
			for(SiegeClanObject siegeClan : defendersRefused)
			{
				_defenderClans.add(new DefenderClan(siegeClan.getClan(), REFUSE, (int) (siegeClan.getDate() / 1000L), siegeClan.getParam(), 0));
			}
		}
		else
		{
			_registrationValid = false;
			_defenderClans = Collections.emptyList();
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// dddddd(dSSddddQddSSd)
		packetWriter.writeD(_id);
		packetWriter.writeD(0x00); // Owner's view
		packetWriter.writeD(_registrationValid);
		packetWriter.writeD(0x00); // Page number
		packetWriter.writeD(_defenderClans.size());
		packetWriter.writeD(_defenderClans.size());
		for(DefenderClan defenderClan : _defenderClans)
		{
			Clan clan = defenderClan._clan;
			packetWriter.writeD(clan.getClanId());
			packetWriter.writeS(clan.getName());
			packetWriter.writeS(clan.getLeaderName());
			packetWriter.writeD(clan.getCrestId());
			packetWriter.writeD(defenderClan._time);
			packetWriter.writeD(defenderClan._type);
			packetWriter.writeD(defenderClan._mercenaryReward > 0); // Have mercenaries recruting
			packetWriter.writeQ(defenderClan._mercenaryReward); // Rawarding Rate %
			packetWriter.writeD(defenderClan._mercenariesCount); // Mercenaries count

			Alliance alliance = clan.getAlliance();
			if(alliance != null)
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