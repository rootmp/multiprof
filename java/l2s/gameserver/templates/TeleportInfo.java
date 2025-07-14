package l2s.gameserver.templates;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.skills.SkillEntry;

public class TeleportInfo
{
	private final int id;
	private final int itemId;
	private final long price;
	private final SkillEntry castingSkill;
	private final int castleId;
	private final int fortressId;
	private final int usableLevel;
	private final List<Location> locations = new ArrayList<>();

	public TeleportInfo(int id, int itemId, long price, SkillEntry castingSkill, int castleId, int fortressId, int usableLevel)
	{
		this.id = id;
		this.itemId = itemId;
		this.price = price;
		this.castingSkill = castingSkill;
		this.castleId = castleId;
		this.fortressId = fortressId;
		this.usableLevel = usableLevel;
	}

	public int getId()
	{
		return id;
	}

	public int getItemId()
	{
		return itemId;
	}

	public long getPrice(Player player)
	{
		if(player == null || !player.hasPremiumAccount())
			return price;
		return (long) (price * .5);
	}

	public SkillEntry getCastingSkill()
	{
		return castingSkill;
	}

	public int getCastleId()
	{
		return castleId;
	}

	public int getFortressId()
	{
		return fortressId;
	}

	public int getUsableLevel()
	{
		return usableLevel;
	}

	public List<Location> getLocations()
	{
		return locations;
	}
}
