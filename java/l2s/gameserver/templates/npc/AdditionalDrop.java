package l2s.gameserver.templates.npc;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.reward.RewardData;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 28.01.2022
 **/
public class AdditionalDrop
{
	private final int minLevel;
	private final int maxLevel;
	private final boolean levelPenalty;
	private final int monsterType;
	private final List<Integer> npcs = new ArrayList<>(0);
	private final List<RewardData> rewardItems = new ArrayList<>(0);

	public AdditionalDrop(int minLevel, int maxLevel, boolean levelPenalty, int monsterType)
	{
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.levelPenalty = levelPenalty;
		this.monsterType = monsterType;
	}

	public boolean isLevelPenalty()
	{
		return levelPenalty;
	}

	public List<Integer> getNpcs()
	{
		return npcs;
	}

	public List<RewardData> getRewardItems()
	{
		return rewardItems;
	}

	public boolean checkMonster(MonsterInstance monster)
	{
		if(minLevel > monster.getLevel())
			return false;
		if(maxLevel < monster.getLevel())
			return false;
		if(!npcs.isEmpty() && !npcs.contains(monster.getNpcId()))
			return false;

		if(monsterType == 0)
			return true;

		if(monsterType == 1)
			return !monster.isRaid();

		if(monsterType == 2)
			return monster.isRaid();

		if(monsterType == 3)
			return monster.getReflection().isMain();

		if(monsterType == 4)
			return !monster.isRaid() && monster.getReflection().isMain();

		if(monsterType == 5)
			return monster.isRaid() && monster.getReflection().isMain();

		if(monsterType == 6)
			return !monster.getReflection().isMain();

		if(monsterType == 7)
			return !monster.isRaid() && !monster.getReflection().isMain();

		if(monsterType == 8)
			return monster.isRaid() && !monster.getReflection().isMain();

		return false;
	}
}
