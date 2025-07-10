package l2s.gameserver.model.actor.instances.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import l2s.gameserver.dao.PvpbookDAO;
import l2s.gameserver.model.Player;

public class Pvpbook
{
	public static final int EXPIRATION_DELAY = (int) TimeUnit.HOURS.toSeconds(24);

	private static final String LOCATION_SHOW_COUNT_VAR = "pvpbook_loc_show_count";
	private static final String TELEPORT_COUNT_VAR = "pvpbook_teleport_count";
	private static final String TELEPORT_HELP_COUNT_VAR = "pvpbook_teleport_help_count";

	private static final int MAX_LOCATION_SHOW_COUNT_PER_DAY = 5;
	private static final int MAX_TELEPORT_COUNT_PER_DAY = 5;
	private static final int MAX_TELEPORT_HELP_COUNT_PER_DAY = 1;

	public static final long LOCATION_SHOW_PRICE = 0;
	public static final long TELEPORT_PRICE = 10;
	public static final int TELEPORT_HELP_PRICE = 100;

	private final Player owner;
	private final Map<Integer, PvpbookInfo> infos = new HashMap<>();

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

	public PvpbookInfo addInfo(Player killed, Player killer, int deathTime, int sharedTime)
	{
		if (!isExpired(deathTime))
		{
			PvpbookInfo pvpbookInfo = new PvpbookInfo(this, killed, killer, deathTime, sharedTime);
			infos.put(pvpbookInfo.getKillerObjectId(), pvpbookInfo);
			return pvpbookInfo;
		}
		return null;
	}

	public PvpbookInfo addInfo(int killedObjectId, int killerObjectId, int deathTime, String killedName, String killerName, int killedLevel, int killerLevel, int killedClassId, int killerClassId, String killedClanName, String killerClanName, int karma, int sharedTime)
	{
		if (!isExpired(deathTime))
		{
			PvpbookInfo pvpbookInfo = new PvpbookInfo(this, killedObjectId, killerObjectId, deathTime, killedName, killerName, killedLevel, killerLevel, killedClassId, killerClassId, killedClanName, killerClanName, karma, sharedTime);
			infos.put(pvpbookInfo.getKillerObjectId(), pvpbookInfo);
			return pvpbookInfo;
		}
		return null;
	}

	public Collection<PvpbookInfo> getInfos(boolean withExpired)
	{
		if (withExpired)
			return infos.values();

		List<PvpbookInfo> tempInfos = new ArrayList<>();
		for (PvpbookInfo pvpbookInfo : infos.values())
		{
			if (!pvpbookInfo.isExpired())
				tempInfos.add(pvpbookInfo);
		}
		return tempInfos;
	}

	public PvpbookInfo getInfo(String killerName)
	{
		for (PvpbookInfo pvpbookInfo : getInfos(false))
		{
			if (pvpbookInfo.getKillerName().equalsIgnoreCase(killerName))
				return pvpbookInfo;
		}
		return null;
	}

	public PvpbookInfo getInfo(int objectId)
	{
		PvpbookInfo pvpbookInfo = infos.get(objectId);
		if (pvpbookInfo != null && !pvpbookInfo.isExpired())
			return pvpbookInfo;
		return null;
	}

	public int getLocationShowCount()
	{
		return MAX_LOCATION_SHOW_COUNT_PER_DAY - owner.getVarInt(LOCATION_SHOW_COUNT_VAR, 0);
	}

	public void reduceLocationShowCount()
	{
		int count = owner.getVarInt(LOCATION_SHOW_COUNT_VAR, 0);
		owner.setVar(LOCATION_SHOW_COUNT_VAR, count + 1);
	}

	public int getTeleportCount()
	{
		return MAX_TELEPORT_COUNT_PER_DAY - owner.getVarInt(TELEPORT_COUNT_VAR, 0);
	}

	public int getTeleportHelpCount()
	{
		return MAX_TELEPORT_HELP_COUNT_PER_DAY - owner.getVarInt(TELEPORT_HELP_COUNT_VAR, 0);
	}

	public void reduceTeleportCount()
	{
		int count = owner.getVarInt(TELEPORT_COUNT_VAR, 0);
		owner.setVar(TELEPORT_COUNT_VAR, count + 1);
	}

	public void reduceTeleportHelpCount()
	{
		int count = owner.getVarInt(TELEPORT_HELP_COUNT_VAR, 0);
		owner.setVar(TELEPORT_HELP_COUNT_VAR, count + 1);
	}

	public void reset()
	{
		owner.unsetVar(LOCATION_SHOW_COUNT_VAR);
		owner.unsetVar(TELEPORT_COUNT_VAR);
		owner.unsetVar(TELEPORT_HELP_COUNT_VAR);
	}

	public static boolean isExpired(int deathTime)
	{
		return (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - deathTime) > EXPIRATION_DELAY;
	}
}
