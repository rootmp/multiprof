package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2s.gameserver.model.entity.events.objects.SiegeClanObject;
import l2s.gameserver.model.entity.residence.Castle;

public class ExPledgeMercenaryMemberList implements IClientOutgoingPacket
{
	private final int receiverObjectId;
	private final int castleId;
	private final int clanId;

	public ExPledgeMercenaryMemberList(Player player, int castleId, int clanId)
	{
		receiverObjectId = player.getObjectId();
		this.castleId = castleId;
		this.clanId = clanId;

		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, castleId);
		if (castle != null)
		{
			CastleSiegeEvent siegeEvent = castle.getSiegeEvent();
			if (siegeEvent != null)
			{
				SiegeClanObject siegeClanObject = siegeEvent.getSiegeClan(CastleSiegeEvent.ATTACKERS, clanId);
				if (siegeClanObject == null)
					siegeClanObject = siegeEvent.getSiegeClan(CastleSiegeEvent.DEFENDERS, clanId);
				if (siegeClanObject != null)
				{
					//
				}
			}
		}

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// stream(ddd(ddSd))
		packetWriter.writeD(castleId);
		packetWriter.writeD(clanId);
		packetWriter.writeD(0);
		/*
		 * for (CastleSiegeMercenaryObject mercenaryObject : mercenaries) {
		 * packetWriter.writeD(receiverObjectId == mercenaryObject.getPlayerObjectId());
		 * packetWriter.writeD(mercenaryObject.getPlayerObjectId()); // TODO: Check.
		 * packetWriter.writeString(mercenaryObject.getName());
		 * packetWriter.writeD(mercenaryObject.getClassId().ordinal()); }
		 */
	}
}
