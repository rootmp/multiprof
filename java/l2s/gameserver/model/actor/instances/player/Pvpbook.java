package l2s.gameserver.model.actor.instances.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import l2s.gameserver.Config;
import l2s.gameserver.dao.PvpbookDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.utils.ItemFunctions;

public class Pvpbook
{
	public static final int EXPIRATION_DELAY = (int) TimeUnit.HOURS.toSeconds(Config.PVPBOOK_EXPIRATION_DELAY);

	public static final int MAX_LOCATION_SHOW_COUNT_PER_DAY = 5;
	public static final int MAX_TELEPORT_COUNT_PER_DAY = 5;
	public static final int MAX_TELEPORT_HELP_COUNT_PER_DAY = 1;

	private final Player owner;
	private final Map<String, PvpbookInfo> infos = new HashMap<>();

	public Pvpbook(Player owner)
	{
		this.owner = owner;
	}

	public Player getOwner()
	{
		return owner;
	}

	public void restore()
	{
		PvpbookDAO.getInstance().restore(owner);
	}

	public void store()
	{
		PvpbookDAO.getInstance().store(owner);
	}

	private String createKey(int killerObjectId, int shareType)
	{
		return killerObjectId + "_" + shareType;
	}

	public PvpbookInfo addInfo(Player killed, Player killer, int deathTime, int sharedTime, int locationShow, int teleportСount, int teleportHelpCount, int shareType, int request_for_help)
	{
		if(!isExpired(deathTime))
		{
			PvpbookInfo pvpbookInfo = new PvpbookInfo(this, killed, killer, deathTime, sharedTime, locationShow, teleportСount, teleportHelpCount, shareType, request_for_help);
			infos.put(createKey(pvpbookInfo.getKillerObjectId(), shareType), pvpbookInfo);
			return pvpbookInfo;
		}
		return null;
	}

	public PvpbookInfo addInfo(int killedObjectId, int killerObjectId, int deathTime, String killedName, String killerName, int killedLevel, int killerLevel, int killedClassId, int killerClassId, String killedClanName, String killerClanName, int karma, int sharedTime, int locationShow, int teleportCount, int teleportHelpCount, int shareType, int request_for_help)
	{
		if(!isExpired(deathTime))
		{
			PvpbookInfo pvpbookInfo = new PvpbookInfo(this, killedObjectId, killerObjectId, deathTime, killedName, killerName, killedLevel, killerLevel, killedClassId, killerClassId, killedClanName, killerClanName, karma, sharedTime, locationShow, teleportCount, teleportHelpCount, shareType, request_for_help);
			infos.put(createKey(pvpbookInfo.getKillerObjectId(), shareType), pvpbookInfo);
			return pvpbookInfo;
		}
		return null;
	}

	public Collection<PvpbookInfo> getInfos(boolean withExpired)
	{
		if(withExpired)
			return infos.values();

		List<PvpbookInfo> tempInfos = new ArrayList<>();
		for(PvpbookInfo pvpbookInfo : infos.values())
		{
			if(!pvpbookInfo.isExpired())
				tempInfos.add(pvpbookInfo);
		}
		return tempInfos;
	}

	public PvpbookInfo getInfo(String killerName, int shareType)
	{
		for(PvpbookInfo pvpbookInfo : getInfos(false))
		{
			if(pvpbookInfo.getKillerName().equalsIgnoreCase(killerName) && pvpbookInfo.getShareType() == shareType)
				return pvpbookInfo;
		}
		return null;
	}

	public PvpbookInfo getInfo(int objectId, int shareType)
	{
		PvpbookInfo pvpbookInfo = infos.get(createKey(objectId, shareType));
		if(pvpbookInfo != null && !pvpbookInfo.isExpired())
			return pvpbookInfo;
		return null;
	}

	public boolean reduceAdenaLocationShowCount(PvpbookInfo pvpbookInfo)
	{
		int adenaToDeduct = getPriceForViewCount(Config.PVPBOOK_ADENA_LOCATION_SHOW, MAX_LOCATION_SHOW_COUNT_PER_DAY
				- pvpbookInfo.getLocationShowCount());
		if(adenaToDeduct > 0 && !owner.reduceAdena(adenaToDeduct, true))
		{
			owner.sendPacket(SystemMsg.NOT_ENOUGH_MONEY_TO_USE_THE_FUNCTION);
			return false;
		}
		return true;
	}

	public int getPriceForViewCount(Map<Integer, Integer> config, int viewCount)
	{
		int price = 0;
		for(Map.Entry<Integer, Integer> entry : config.entrySet())
		{
			if(viewCount >= entry.getKey())
				price = entry.getValue();
			else
				break;
		}
		return price;
	}

	public boolean reduceLcoinTeleportCount(PvpbookInfo pvpbookInfo)
	{
		int lcoinToDeduct = getPriceForViewCount(Config.PVPBOOK_LCOIN_TELEPORT_COUNT, MAX_TELEPORT_COUNT_PER_DAY - pvpbookInfo.getTeleportCount());

		if(lcoinToDeduct > 0)
		{
			if(!ItemFunctions.deleteItem(owner, 91663, lcoinToDeduct))
			{
				owner.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_L2_COINS_ADD_MORE_L2_COINS_AND_TRY_AGAIN);
				return false;
			}
		}

		return true;
	}

	public void reset()
	{
		for(PvpbookInfo info : infos.values())
		{
			info.setTeleportCount(MAX_TELEPORT_COUNT_PER_DAY);
			info.setTeleportHelpCount(MAX_TELEPORT_HELP_COUNT_PER_DAY);
			info.setLocationShowCount(MAX_LOCATION_SHOW_COUNT_PER_DAY);
		}
		store();
	}

	public static boolean isExpired(int deathTime)
	{
		return (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - deathTime) > EXPIRATION_DELAY;
	}
}
