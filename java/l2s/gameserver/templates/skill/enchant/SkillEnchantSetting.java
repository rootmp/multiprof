package l2s.gameserver.templates.skill.enchant;

public class SkillEnchantSetting
{
	private int grade;
	private int sublevel;
	private int successItemId;
	private int itemId;
	private long count;
	private long adena;
	private int chance;

	public SkillEnchantSetting(int grade, int sublevel, int successItemId, int itemId, long count, long adena, int chance)
	{
		this.grade = grade;
		this.sublevel = sublevel;
		this.successItemId = successItemId;
		this.itemId = itemId;
		this.count = count;
		this.adena = adena;
		this.chance = chance;
	}

	public SkillEnchantSetting(int grade, int sublevel, int itemId, long count, long adena)
	{
		this.grade = grade;
		this.sublevel = sublevel;
		this.itemId = itemId;
		this.count = count;
		this.adena = adena;
	}

	public int getGrade()
	{
		return grade;
	}

	public void setGrade(int grade)
	{
		this.grade = grade;
	}

	public int getSublevel()
	{
		return sublevel;
	}

	public void setSublevel(int sublevel)
	{
		this.sublevel = sublevel;
	}

	public int getSuccessItemId()
	{
		return successItemId;
	}

	public void setSuccessItemId(int successItemId)
	{
		this.successItemId = successItemId;
	}

	public int getItemId()
	{
		return itemId;
	}

	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}

	public long getCount()
	{
		return count;
	}

	public void setCount(long count)
	{
		this.count = count;
	}

	public long getAdena()
	{
		return adena;
	}

	public void setAdena(long adena)
	{
		this.adena = adena;
	}

	public int getChance()
	{
		return chance;
	}

	public void setChance(int chance)
	{
		this.chance = chance;
	}

	@Override
	public String toString()
	{
		return "SkillEnchantSetting{" +
				"grade=" + grade +
				", sublevel=" + sublevel +
				", successItemId=" + successItemId +
				", itemId=" + itemId +
				", count=" + count +
				", adena=" + adena +
				", chance=" + chance +
				'}';
	}
}
