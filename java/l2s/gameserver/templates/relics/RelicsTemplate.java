package l2s.gameserver.templates.relics;

import java.util.List;
import java.util.Map;

import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.skills.SkillEntry;

public class RelicsTemplate
{
	private int relics_id;
	private int item_id;
	private int grade;
	private Map<Integer, int[]> skill_id;
	private List<Integer> enchanted;
	private int npc_id;
	private int level;
	private int sort_order;

	public RelicsTemplate(int relicsId, int itemId, int grade, List<Integer> enchanted, int npcId, int level, int sortOrder, Map<Integer, int[]> skills)
	{
		this.relics_id = relicsId;
		this.item_id = itemId;
		this.grade = grade;
		this.enchanted = enchanted;
		this.npc_id = npcId;
		this.level = level;
		this.sort_order = sortOrder;
		this.skill_id = skills;
	}

	public int getRelicsId()
	{
		return relics_id;
	}

	public int getItemId()
	{
		return item_id;
	}

	public int getGrade()
	{
		return grade;
	}

	public List<Integer> getEnchanted()
	{
		return enchanted;
	}

	public int getNpcId()
	{
		return npc_id;
	}

	public int getLevel()
	{
		return level;
	}

	public int getSortOrder()
	{
		return sort_order;
	}

	public SkillEntry getSkillByIndex(int index)
	{
		int[] skill = skill_id.get(index);
		if(skill != null)
			return SkillHolder.getInstance().getSkillEntry(skill[0], skill[1]);
		return null;
	}
}
