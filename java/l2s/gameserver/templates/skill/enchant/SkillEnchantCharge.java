package l2s.gameserver.templates.skill.enchant;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.utils.Util;

public class SkillEnchantCharge
{
	private int groupItem;
	private Map<Integer, Pair<Integer, Integer>> expGrade = new HashMap<>();
	private int commissionItemId;
	private int groupId;

	public SkillEnchantCharge(StatsSet statsSet)
	{
		groupItem = statsSet.getInteger("group_item");
		expGrade = Util.parseExpGrade(statsSet.getString("exp_grade"));
		commissionItemId = statsSet.getInteger("commission_item_id");
		groupId = statsSet.getInteger("group_id");
	}

	public int getGroupItem()
	{
		return groupItem;
	}

	public Map<Integer, Pair<Integer, Integer>> getExpGrade()
	{
		return expGrade;
	}

	public int getCommissionItemId()
	{
		return commissionItemId;
	}

	public int getGroupId()
	{
		return groupId;
	}
}
