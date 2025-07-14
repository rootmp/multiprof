package l2s.gameserver.enums;

public enum LampType
{
	RED(1, 52038),
	PURPLE(2, 52039),
	BLUE(3, 52040),
	GREEN(4, 52041);

	private final int _grade;
	private int _skill_id;

	LampType(int grade, int skill_id)
	{
		_grade = grade;
		_skill_id = skill_id;
	}

	public int getGrade()
	{
		return _grade;
	}

	public int getSkillId()
	{
		return _skill_id;
	}
}