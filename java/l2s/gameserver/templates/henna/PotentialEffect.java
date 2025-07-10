package l2s.gameserver.templates.henna;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 04.11.2021
 **/
public class PotentialEffect
{
	private final int id;
	private final int slotId;
	private final int maxSkillLevel;
	private final int skillId;

	public PotentialEffect(int id, int slotId, int maxSkillLevel, int skillId)
	{
		this.id = id;
		this.slotId = slotId;
		this.maxSkillLevel = maxSkillLevel;
		this.skillId = skillId;
	}

	public int getId()
	{
		return id;
	}

	public int getSlotId()
	{
		return slotId;
	}

	public int getMaxSkillLevel()
	{
		return maxSkillLevel;
	}

	public int getSkillId()
	{
		return skillId;
	}
}
