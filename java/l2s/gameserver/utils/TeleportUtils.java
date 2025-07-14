package l2s.gameserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.MapRegionManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.TeleportPoint;
import l2s.gameserver.model.base.RestartType;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.entity.residence.Residence;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.templates.mapregion.RestartArea;
import l2s.gameserver.templates.mapregion.RestartPoint;

public class TeleportUtils
{
	private static final Logger _log = LoggerFactory.getLogger(TeleportUtils.class);

	public final static Location DEFAULT_RESTART = new Location(17817, 170079, -3530);

	public static TeleportPoint getRestartPoint(Player player, RestartType restartType)
	{
		return getRestartPoint(player, player.getLoc(), restartType);
	}

	public static TeleportPoint getRestartPoint(Player player, Location from, RestartType restartType)
	{
		final TeleportPoint teleportPoint = new TeleportPoint();

		Reflection r = player.getReflection();
		if(!r.isMain())
		{
			if(r.getCoreLoc() != null)
				return teleportPoint.setLoc(r.getCoreLoc());
			else if(r.getReturnLoc() != null)
				return teleportPoint.setLoc(r.getReturnLoc());
		}

		Clan clan = player.getClan();
		if(clan != null)
		{
			int residenceId = 0;
			if(restartType == RestartType.TO_CLANHALL) // If teleport to clan hall
				residenceId = clan.getHasHideout();
			else if(restartType == RestartType.TO_CASTLE) // If teleport to castle
				residenceId = clan.getCastle();

			if(residenceId != 0)
			{
				Residence residence = ResidenceHolder.getInstance().getResidence(residenceId);
				if(residence != null)
				{
					Reflection reflection = residence.getReflection(clan.getClanId());
					if(reflection != null)
					{
						teleportPoint.setLoc(residence.getOwnerRestartPoint());
						teleportPoint.setReflection(reflection);
						return teleportPoint;
					}
				}
			}
		}

		if(player.isPK())
		{
			if(player.getPKRestartPoint() != null)
				return teleportPoint.setLoc(player.getPKRestartPoint());
		}
		else
		{
			if(player.getRestartPoint() != null)
				return teleportPoint.setLoc(player.getRestartPoint());
		}

		RestartArea ra = MapRegionManager.getInstance().getRegionData(RestartArea.class, from);
		if(ra != null)
		{
			RestartPoint rp = ra.getRestartPoint().get(player.getRace());

			Location restartPoint = Rnd.get(rp.getRestartPoints());
			Location PKrestartPoint = Rnd.get(rp.getPKrestartPoints());

			return teleportPoint.setLoc(player.isPK() ? PKrestartPoint : restartPoint);
		}

		_log.warn("Cannot find restart location from coordinates: " + from + "!");

		return teleportPoint.setLoc(DEFAULT_RESTART);
	}
}