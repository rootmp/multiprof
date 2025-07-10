package l2s.gameserver.network.l2.c2s;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.dao.SiegeClanDAO;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2s.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.entity.events.objects.CastleSiegeClanObject;
import l2s.gameserver.model.entity.events.objects.SiegeClanObject;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.entity.residence.ClanHall;
import l2s.gameserver.model.entity.residence.Residence;
import l2s.gameserver.model.entity.residence.ResidenceType;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.Privilege;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.CastleSiegeAttackerListPacket;
import l2s.gameserver.network.l2.s2c.ExMercenaryCastlewarCastleSiegeAttackerList;
import l2s.gameserver.network.l2.s2c.ExMercenaryCastlewarCastleSiegeDefenderList;

/**
 * @author VISTALL
 */
public class RequestJoinCastleSiege extends L2GameClientPacket
{
	private int _id;
	private boolean _isAttacker;
	private boolean _isJoining;

	@Override
	protected boolean readImpl()
	{
		_id = readD();
		_isAttacker = readD() == 1;
		_isJoining = readD() == 1;
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		if (!player.hasPrivilege(Privilege.CS_FS_SIEGE_WAR))
		{
			player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}

		Residence residence = ResidenceHolder.getInstance().getResidence(_id);
		if (residence == null)
		{
			player.sendActionFailed();
			return;
		}

		if (residence.getType() == ResidenceType.CASTLE)
			registerAtCastle(player, (Castle) residence, _isAttacker, _isJoining);
		else if (residence.getType() == ResidenceType.CLANHALL && _isAttacker)
			registerAtClanHall(player, (ClanHall) residence, _isJoining);
	}

	private static void registerAtCastle(Player player, Castle castle, boolean attacker, boolean join)
	{
		CastleSiegeEvent siegeEvent = castle.getSiegeEvent();
		if (siegeEvent == null)
			return;

		Clan playerClan = player.getClan();

		if (playerClan.isPlacedForDisband())
		{
			player.sendPacket(SystemMsg.YOU_HAVE_ALREADY_REQUESTED_THE_DISSOLUTION_OF_YOUR_CLAN);
			return;
		}

		if (join)
		{
			Residence registeredCastle = null;
			for (Residence residence : ResidenceHolder.getInstance().getResidenceList(Castle.class))
			{
				CastleSiegeEvent residenceSiegeEvent = residence.getSiegeEvent();
				if (residenceSiegeEvent != null)
				{
					SiegeClanObject tempCastle = residenceSiegeEvent.getSiegeClan(CastleSiegeEvent.ATTACKERS, playerClan);

					if (tempCastle == null)
						tempCastle = residenceSiegeEvent.getSiegeClan(CastleSiegeEvent.DEFENDERS, playerClan);

					if (tempCastle == null)
						tempCastle = residenceSiegeEvent.getSiegeClan(CastleSiegeEvent.DEFENDERS_WAITING, playerClan);

					if (tempCastle != null)
						registeredCastle = residence;
				}
			}

			if (!siegeEvent.canRegisterOnSiege(player, playerClan, attacker))
				return;

			if (registeredCastle != null)
			{
				player.sendPacket(SystemMsg.YOU_HAVE_ALREADY_REQUESTED_A_CASTLE_SIEGE);
				return;
			}

			if (castle.getSiegeDate().getTimeInMillis() == 0)
			{
				player.sendPacket(SystemMsg.THIS_IS_NOT_THE_TIME_FOR_SIEGE_REGISTRATION_AND_SO_REGISTRATION_AND_CANCELLATION_CANNOT_BE_DONE);
				return;
			}

			if (siegeEvent.isRegistrationOver())
			{
				player.sendPacket(SystemMsg.YOU_ARE_TOO_LATE_THE_REGISTRATION_PERIOD_IS_OVER);
				return;
			}

			CastleSiegeClanObject siegeClan;
			if (attacker)
			{
				int allSize = siegeEvent.getObjects(CastleSiegeEvent.ATTACKERS).size();
				if (allSize >= CastleSiegeEvent.MAX_SIEGE_CLANS)
				{
					player.sendPacket(SystemMsg.NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_ATTACKER_SIDE);
					return;
				}
				siegeClan = siegeEvent.newSiegeClan(CastleSiegeEvent.ATTACKERS, playerClan.getClanId(), 0, System.currentTimeMillis());
			}
			else
			{
				siegeClan = siegeEvent.newSiegeClan(CastleSiegeEvent.DEFENDERS_WAITING, playerClan.getClanId(), 0, System.currentTimeMillis());
			}

			// Удаляем наемников
			List<CastleSiegeClanObject> siegeClanObjects = new ArrayList<>();
			siegeClanObjects.addAll(siegeEvent.getObjects(CastleSiegeEvent.ATTACKERS));
			siegeClanObjects.addAll(siegeEvent.getObjects(CastleSiegeEvent.DEFENDERS));
			for (UnitMember unitMember : playerClan)
			{
				for (CastleSiegeClanObject sc : siegeClanObjects)
				{
					if (sc.removeMercenary(unitMember.getObjectId()))
					{
						Player cl = sc.getClan().getLeader().getPlayer();
						if (cl != null)
							cl.sendPacket(SystemMsg.THE_CHARACTER_IS_NOT_A_MERCENARY_ANYMORE_BECAUSE_HIS_HER_CLAN_TAKES_PART_IN_THE_SIEGE_);
					}
				}
			}

			siegeEvent.addObject(siegeClan.getType(), siegeClan);

			SiegeClanDAO.getInstance().insert(castle, siegeClan);

			if (siegeClan.getType().equals(SiegeEvent.ATTACKERS))
				player.sendPacket(new ExMercenaryCastlewarCastleSiegeAttackerList(castle));
			else
				player.sendPacket(new ExMercenaryCastlewarCastleSiegeDefenderList(castle));
		}
		else
		{
			SiegeClanObject siegeClan;
			if (attacker)
				siegeClan = siegeEvent.getSiegeClan(CastleSiegeEvent.ATTACKERS, playerClan);
			else
			{
				siegeClan = siegeEvent.getSiegeClan(CastleSiegeEvent.DEFENDERS, playerClan);
				if (siegeClan == null)
					siegeClan = siegeEvent.getSiegeClan(CastleSiegeEvent.DEFENDERS_WAITING, playerClan);
				if (siegeClan == null)
					siegeClan = siegeEvent.getSiegeClan(CastleSiegeEvent.DEFENDERS_REFUSED, playerClan);
			}

			if (siegeClan == null)
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_YET_REGISTERED_FOR_THE_CASTLE_SIEGE);
				return;
			}

			if (siegeEvent.isRegistrationOver())
			{
				player.sendPacket(SystemMsg.YOU_ARE_TOO_LATE_THE_REGISTRATION_PERIOD_IS_OVER);
				return;
			}

			if (siegeClan.getParam() > 0)
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CANCEL_THE_SIEGE_WHEN_YOU_ARE_RECRUITING_MERCENARIES);
				return;
			}

			siegeEvent.removeObject(siegeClan.getType(), siegeClan);

			SiegeClanDAO.getInstance().delete(castle, siegeClan);
			if (siegeClan.getType().equals(SiegeEvent.ATTACKERS))
				player.sendPacket(new ExMercenaryCastlewarCastleSiegeAttackerList(castle));
			else
				player.sendPacket(new ExMercenaryCastlewarCastleSiegeDefenderList(castle));
		}
	}

	private static void registerAtClanHall(Player player, ClanHall clanHall, boolean join)
	{
		ClanHallSiegeEvent siegeEvent = clanHall.getSiegeEvent();
		if (siegeEvent == null)
			return;

		Clan playerClan = player.getClan();

		SiegeClanObject siegeClan = siegeEvent.getSiegeClan(CastleSiegeEvent.ATTACKERS, playerClan);

		if (join)
		{
			if (playerClan.getHasHideout() != 0)
			{
				player.sendPacket(SystemMsg.A_CLAN_THAT_OWNS_A_CLAN_HALL_MAY_NOT_PARTICIPATE_IN_A_CLAN_HALL_SIEGE);
				return;
			}

			if (siegeClan != null)
			{
				player.sendPacket(SystemMsg.YOU_HAVE_ALREADY_REQUESTED_A_CASTLE_SIEGE);
				return;
			}

			if (playerClan.getLevel() < 4)
			{
				player.sendPacket(SystemMsg.ONLY_CLANS_WHO_ARE_LEVEL_4_OR_ABOVE_CAN_REGISTER_FOR_BATTLE_AT_DEVASTATED_CASTLE_AND_FORTRESS_OF_THE_DEAD);
				return;
			}

			if (siegeEvent.isRegistrationOver())
			{
				player.sendPacket(SystemMsg.YOU_ARE_TOO_LATE_THE_REGISTRATION_PERIOD_IS_OVER);
				return;
			}

			int allSize = siegeEvent.getObjects(ClanHallSiegeEvent.ATTACKERS).size();
			if (allSize >= CastleSiegeEvent.MAX_SIEGE_CLANS)
			{
				player.sendPacket(SystemMsg.NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_ATTACKER_SIDE);
				return;
			}

			siegeClan = siegeEvent.newSiegeClan(ClanHallSiegeEvent.ATTACKERS, playerClan.getClanId(), 0, System.currentTimeMillis());
			siegeEvent.addObject(ClanHallSiegeEvent.ATTACKERS, siegeClan);

			SiegeClanDAO.getInstance().insert(clanHall, siegeClan);
		}
		else
		{
			if (siegeClan == null)
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_YET_REGISTERED_FOR_THE_CASTLE_SIEGE);
				return;
			}

			if (siegeEvent.isRegistrationOver())
			{
				player.sendPacket(SystemMsg.YOU_ARE_TOO_LATE_THE_REGISTRATION_PERIOD_IS_OVER);
				return;
			}

			siegeEvent.removeObject(siegeClan.getType(), siegeClan);

			SiegeClanDAO.getInstance().delete(clanHall, siegeClan);
		}

		player.sendPacket(new CastleSiegeAttackerListPacket(clanHall));
	}
}