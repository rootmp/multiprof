package l2s.gameserver.model.entity.events.objects;

import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;

public class CastleSiegeMercenaryObject
{
	public static String getName(int mercenaryId)
	{
		return "***-" + String.format("%03d", mercenaryId);
	}

	private final int playerObjectId;
	private final int mercenaryId;
	private final int clanId;
	private String name;
	private ClassId classId;

	public CastleSiegeMercenaryObject(int playerObjectId, int mercenaryId, int clanId, ClassId classId)
	{
		this.playerObjectId = playerObjectId;
		this.mercenaryId = mercenaryId;
		this.clanId = clanId;
		name = getName(mercenaryId);
		this.classId = classId;
	}

	public CastleSiegeMercenaryObject(Player player, int mercenaryId, int clanId)
	{
		this(player.getObjectId(), mercenaryId, clanId, player.getClassId());
	}

	public int getPlayerObjectId()
	{
		return playerObjectId;
	}

	public Player getPlayer()
	{
		return GameObjectsStorage.getPlayer(getPlayerObjectId());
	}

	public int getMercenaryId()
	{
		return mercenaryId;
	}

	public int getClanId()
	{
		return clanId;
	}

	public String getName()
	{
		return name;
	}

	public ClassId getClassId()
	{
		Player player = getPlayer();
		if(player != null)
			classId = player.getClassId();
		return classId;
	}
}
