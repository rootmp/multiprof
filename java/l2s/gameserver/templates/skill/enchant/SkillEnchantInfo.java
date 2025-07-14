package l2s.gameserver.templates.skill.enchant;

public class SkillEnchantInfo
{
	private int nSkillID;
	private int nSubLevel;
	private int nEXP;

	public SkillEnchantInfo(int skillID, int subLevel, int exp)
	{
		this.nSkillID = skillID;
		this.nSubLevel = subLevel;
		this.nEXP = exp;
	}

	public SkillEnchantInfo(int skillID, int subLevel)
	{
		nSkillID = skillID;
		nSubLevel = subLevel;
		nEXP = 0;
	}

	public int getSkillID()
	{
		return nSkillID;
	}

	public int getSubLevel()
	{
		return nSubLevel;
	}

	public int getEXP()
	{
		return nEXP;
	}

	public String toString()
	{
		return nSkillID + ";" + nSubLevel + ";" + nEXP;
	}

	public int getChance()
	{
		switch(getSubLevel())
		{
			case 0:
				return 5000;
			case 1001:
				return 3000;
			case 1002:
				return 1000;
			default:
				break;
		}
		return 0;
	}

	public void addExp(int exp)
	{
		nEXP = Math.min(nEXP + exp, getMaxEXP());
	}

	public int getMaxEXP()
	{
		return 900000;
	}

	public void setExp(int exp)
	{
		nEXP = exp;
	}
}
