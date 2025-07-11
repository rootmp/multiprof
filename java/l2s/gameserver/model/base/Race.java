package l2s.gameserver.model.base;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;

/**
 * This class defines all races (human, elf, darkelf, orc, dwarf) that a player
 * can chose.<BR>
 * <BR>
 */
public enum Race
{
	HUMAN,
	ELF,
	DARKELF,
	ORC,
	DWARF,
	KAMAEL,
	UNK6,
	UNK7,
	UNK8,
	UNK9,
	UNK10,
	UNK11,
	UNK12,
	UNK13,
	UNK14,
	UNK15,
	UNK16,
	UNK17,
	UNK18,
	UNK19,
	UNK20,
	UNK21,
	UNK22,
	UNK23,
	UNK24,
	UNK25,
	UNK26,
	UNK27,
	UNK28,
	UNK29,
	SYLPH,
	/*31*/ highelf;

	public static final Race[] VALUES = values();

	public final String getName(Player player)
	{
		return new CustomMessage("l2s.gameserver.model.base.Race.name." + ordinal()).toString(player);
	}

	public int getId()
	{
		return ordinal();
	}
	
}