package l2s.gameserver.templates;

import l2s.gameserver.model.Player;

public class PlayerKiller
{
	private int objectId;
	private String name;
	private int level;
	private int classId;
	private int x;
	private int y;
	private int z;

	public PlayerKiller(Player player)
	{
		this.objectId = player.getObjectId();
		this.name = player.getName();
		this.level = player.getLevel();
		this.classId = player.getClassId().getId();
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
	}

	public int getObjectId()
	{
		return objectId;
	}

	public String getName()
	{
		return name;
	}

	public int getLevel()
	{
		return level;
	}

	public int getClassId()
	{
		return classId;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZ()
	{
		return z;
	}
}
