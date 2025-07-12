package l2s.gameserver.model.base;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;

public enum ClassType
{
	FIGHTER,
	MYSTIC,
	DEATH_KNIGHT,
	VANGUARD_RIDER, 
	WARG;

	public static final ClassType[] VALUES = values();

	public boolean isMagician()
	{
		return ((this != FIGHTER) && (this != DEATH_KNIGHT) && (this != VANGUARD_RIDER));
	}

	public final String getName(Player player)
	{
		return new CustomMessage("l2s.gameserver.model.base.ClassType.name." + ordinal()).toString(player);
	}
}