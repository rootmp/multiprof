package l2s.gameserver.enums;

public enum SkillDirectionType
{
	FRONT("front"),
	SIDE("side"),
	BACK("back"),
	LEFT("left"),
	RIGHT("right"),
	NONE("none");

	String pst_name;

	SkillDirectionType(String s)
	{
		pst_name = s;
	}

	public static SkillDirectionType findByName(String name)
	{
		for(SkillDirectionType dir : SkillDirectionType.values())
		{
			if(dir.pst_name.equalsIgnoreCase(name))
				return dir;
		}
		return SkillDirectionType.NONE;
	}
}