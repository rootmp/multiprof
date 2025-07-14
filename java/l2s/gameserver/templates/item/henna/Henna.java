package l2s.gameserver.templates.item.henna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import l2s.dataparser.data.common.ItemRequiredId;
import l2s.dataparser.data.holder.dyedata.DyeData;
import l2s.dataparser.data.pch.LinkerFactory;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.base.BaseStats;

public class Henna
{
	private final int _dyeId;
	private final int _dyeItemId;
	private final int _patternLevel;
	private final Map<BaseStats, Integer> _baseStats = new HashMap<>();
	private final ItemRequiredId[] _wearFee;
	private ItemRequiredId[] _cancelFee;

	private final int _cancelCount;
	private final List<Integer> _wearClass;
	private int _need_count;
	private final String[] _dye_skill;

	public Henna(DyeData dye)
	{
		_dyeId = dye.dye_id;
		_dyeItemId = dye.dye_item_id;
		_patternLevel = dye.dye_level;
		_baseStats.put(BaseStats.STR, dye.str);
		_baseStats.put(BaseStats.CON, dye.con);
		_baseStats.put(BaseStats.DEX, dye.dex);
		_baseStats.put(BaseStats.INT, dye._int);
		_baseStats.put(BaseStats.MEN, dye.men);
		_baseStats.put(BaseStats.WIT, dye.wit);
		_wearFee = dye.wear_fee;
		_cancelFee = dye.cancel_fee;
		_cancelCount = dye.cancel_count;
		_need_count = dye.need_count;
		_dye_skill = dye.dye_skill;
		_wearClass = Arrays.stream(dye.wear_class).boxed().collect(Collectors.toList());
	}

	public List<Skill> convertDyeSkillToSkills(String[] _dye_skill)
	{
		List<Skill> t_skills = new ArrayList<>();
		if(_dye_skill != null)
		{
			for(String tmp_skill : _dye_skill)
			{
				if(tmp_skill != null && !tmp_skill.isBlank() && !tmp_skill.equalsIgnoreCase("none"))
				{
					Skill skill = attachSkill(tmp_skill);
					if(skill != null)
						t_skills.add(skill);
				}
			}
		}
		return t_skills;
	}

	private Skill attachSkill(String dye_skill)
	{
		int[] skill_param = LinkerFactory.getInstance().skillPchIdfindClearValue(dye_skill);
		return skill_param != null ? SkillHolder.getInstance().getSkill(skill_param[0], skill_param[1]) : null;
	}

	/**
	 * @return the dye Id.
	 */
	public int getDyeId()
	{
		return _dyeId;
	}

	/**
	 * @return the item Id, required for this dye.
	 */
	public int getDyeItemId()
	{
		return _dyeItemId;
	}

	public int getBaseStats(BaseStats stat)
	{
		return !_baseStats.containsKey(stat) ? 0 : _baseStats.get(stat).intValue();
	}

	public Map<BaseStats, Integer> getBaseStats()
	{
		return _baseStats;
	}

	/**
	 * @return the wear fee, cost for adding this dye to the player.
	 */
	public ItemRequiredId[] getWearFee()
	{
		return _wearFee;
	}

	public ItemRequiredId getWearFee(int _nCostItemId)
	{
		if(_wearFee == null)
			return null;
		for(ItemRequiredId item : _wearFee)
			if(item.itemName == _nCostItemId)
				return item;
		return null;
	}

	/**
	 * @return the cancel fee, cost for removing this dye from the player.
	 */
	public ItemRequiredId[] getCancelFee()
	{
		return _cancelFee;
	}

	public ItemRequiredId getCancelFee(int _nCostItemId)
	{
		if(_cancelFee == null)
			return null;
		for(ItemRequiredId item : _cancelFee)
			if(item.itemName == _nCostItemId)
				return item;
		return null;
	}

	/**
	 * @return the cancel count, the retrieved amount of dye items after removing the dye.
	 */
	public int getCancelCount()
	{
		return _cancelCount;
	}

	/**
	 * @return the skills related to this dye.
	 */
	public List<Skill> getSkills()
	{
		return convertDyeSkillToSkills(_dye_skill);
	}

	/**
	 * @return the list with the allowed classes to wear this dye.
	 */
	public List<Integer> getAllowedWearClass()
	{
		return _wearClass;
	}

	/**
	 * @param c the class trying to wear this dye.
	 * @return {@code true} if the player is allowed to wear this dye, {@code false} otherwise.
	 */
	public boolean isAllowedClass(Player c)
	{
		return _wearClass.contains(c.getClassId().level());
	}

	/**
	 * @param wearClassIds the list of classes that can wear this dye.
	 */
	public void setWearClassIds(List<Integer> wearClassIds)
	{
		_wearClass.addAll(wearClassIds);
	}

	public int getPatternLevel()
	{
		return _patternLevel;
	}

	public int getNeedCount()
	{
		return _need_count;
	}
}
