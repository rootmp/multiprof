package l2s.gameserver.enums;

public enum DebuffType
{
	GOOD("slot_buff"),
	BAD("slot_debuff"),
	PHYSICAL_MES("speed_down"), //??
	MENTAL_MES("multi_buff"), //??
	MAX("slot_all");

	String type = "";

	DebuffType(String string)
	{
		type = string;
	}

	public static DebuffType findByString(String name)
	{
		for(DebuffType type : DebuffType.values())
		{
			if(type.type.equalsIgnoreCase(name))
				return type;
		}
		return null;
	}
}