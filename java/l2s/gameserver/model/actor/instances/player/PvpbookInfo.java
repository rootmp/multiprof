package l2s.gameserver.model.actor.instances.player;

import l2s.gameserver.dao.PvpbookDAO;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;

public class PvpbookInfo
{
	private final Pvpbook pvpbook;
	private final int killedObjectId;
	private final int killerObjectId;
	private int deathTime;
	private final int sharedTime;

	private String killedName;
	private String killerName;
	private int killedLevel;
	private int killerLevel;
	private int killedClassId;
	private int killerClassId;
	private String killedClanName;
	private String killerClanName;
	private int _karma;
	private boolean revenged;
	private int locationShow;
	private int teleportCount;
	private int teleportHelpCount;
	private int shareType;
	private int request_for_help;

	protected PvpbookInfo(Pvpbook pvpbook, Player killed, Player killer, int deathTime, int sharedTime, int locationShow, int teleportCount, int teleportHelpCount, int shareType, int request_for_help)
	{
		this.pvpbook = pvpbook;
		killedObjectId = killed.getObjectId();
		killerObjectId = killer.getObjectId();
		this.deathTime = deathTime;
		killedName = killed.getVisibleName(pvpbook.getOwner());
		killerName = killer.getVisibleName(pvpbook.getOwner());
		killedLevel = killed.getLevel();
		killerLevel = killer.getLevel();
		killedClassId = killed.getClassId().getId();
		killerClassId = killer.getClassId().getId();
		Clan clan = killer.getClan();
		killerClanName = clan != null ? clan.getName() : "";
		clan = killed.getClan();
		killedClanName = clan != null ? clan.getName() : "";
		_karma = killer.getKarma();
		this.sharedTime = sharedTime;
		this.locationShow = locationShow;
		this.teleportCount = teleportCount;
		this.teleportHelpCount = teleportHelpCount;
		this.shareType = shareType;
		this.request_for_help = request_for_help;
	}

	protected PvpbookInfo(Pvpbook pvpbook, int killedObjectId, int killerObjectId, int deathTime, String killedName, String killerName, int killedLevel, int killerLevel, int killedClassId, int killerClassId, String killedClanName, String killerClanName, int karma, int sharedTime, int locationShow, int teleportCount, int teleportHelpCount, int shareType, int request_for_help)
	{
		this.pvpbook = pvpbook;
		this.killedObjectId = killedObjectId;
		this.killerObjectId = killerObjectId;
		this.deathTime = deathTime;
		this.killedName = killedName;
		this.killerName = killerName;
		this.killedLevel = killedLevel;
		this.killerLevel = killerLevel;
		this.killedClassId = killedClassId;
		this.killerClassId = killerClassId;
		this.killedClanName = killedClanName;
		this.killerClanName = killerClanName;
		this._karma = karma;
		this.sharedTime = sharedTime;
		this.locationShow = locationShow;
		this.teleportCount = teleportCount;
		this.teleportHelpCount = teleportHelpCount;
		this.shareType = shareType;
		this.request_for_help = request_for_help;
	}

	public int getKilledObjectId()
	{
		return killedObjectId;
	}

	public int getKillerObjectId()
	{
		return killerObjectId;
	}

	public Player getKilled()
	{
		return GameObjectsStorage.getPlayer(killedObjectId);
	}

	public Player getKiller()
	{
		return GameObjectsStorage.getPlayer(killerObjectId);
	}

	public int getDeathTime()
	{
		return deathTime;
	}

	public void setDeathTime(int time)
	{
		deathTime = time;
	}

	public String getKilledName()
	{
		Player player = getKilled();
		if(player != null)
			killedName = player.getVisibleName(pvpbook.getOwner());
		return killedName;
	}

	public String getKillerName()
	{
		Player player = getKiller();
		if(player != null)
			killerName = player.getVisibleName(pvpbook.getOwner());
		return killerName;
	}

	public int getKilledLevel()
	{
		Player player = getKilled();
		if(player != null)
			killedLevel = player.getLevel();
		return killedLevel;
	}

	public int getKillerLevel()
	{
		Player player = getKiller();
		if(player != null)
			killerLevel = player.getLevel();
		return killerLevel;
	}

	public int getKilledClassId()
	{
		Player player = getKilled();
		if(player != null)
			killedClassId = player.getClassId().getId();
		return killedClassId;
	}

	public int getKillerClassId()
	{
		Player player = getKiller();
		if(player != null)
			killerClassId = player.getClassId().getId();
		return killerClassId;
	}

	public String getKilledClanName()
	{
		Player player = getKilled();
		if(player != null)
		{
			Clan clan = player.getClan();
			killedClanName = clan != null ? clan.getName() : "";
		}
		return killedClanName;
	}

	public String getKillerClanName()
	{
		Player player = getKiller();
		if(player != null)
		{
			Clan clan = player.getClan();
			killerClanName = clan != null ? clan.getName() : "";
		}
		return killerClanName;
	}

	public boolean isOnline()
	{
		Player player = getKiller();
		if(player != null)
			return player.isOnline();
		return false;
	}

	public int getKarma()
	{
		return _karma;
	}

	public int getSharedTime()
	{
		return sharedTime;
	}

	public boolean isExpired()
	{
		return Pvpbook.isExpired(deathTime);
	}

	public boolean isRevenged()
	{
		return revenged;
	}

	public void setRevenged(boolean revenged)
	{
		this.revenged = revenged;
	}

	public int getLocationShowCount()
	{
		return locationShow;
	}

	public void setLocationShowCount(int count)
	{
		locationShow = count;
	}

	public void reduceLocationShowCount()
	{
		locationShow--;
		PvpbookDAO.getInstance().insert(pvpbook.getOwner(), this);
	}

	public int getTeleportCount()
	{
		return teleportCount;
	}

	public void reduceTeleportCount()
	{
		teleportCount--;
		PvpbookDAO.getInstance().insert(pvpbook.getOwner(), this);
	}

	public void setTeleportCount(int count)
	{
		teleportCount = count;
	}

	public int getTeleportHelpCount()
	{
		return teleportHelpCount;
	}

	public void reduceTeleportHelpCount()
	{
		teleportHelpCount--;
		PvpbookDAO.getInstance().insert(pvpbook.getOwner(), this);
	}

	public void setTeleportHelpCount(int count)
	{
		teleportHelpCount = count;
	}

	public int getShareType()
	{
		return shareType;
	}

	public int isRequestForHelp()
	{
		return request_for_help;
	}

	public void setRequestForHelp()
	{
		request_for_help = 1;
		PvpbookDAO.getInstance().insert(pvpbook.getOwner(), this);
	}
}
