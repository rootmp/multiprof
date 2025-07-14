/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.gameserver.skills.effects.instant;

import java.util.HashSet;
import java.util.Set;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class i_call_skill_by_target extends i_abstract_effect
{
	private final SkillEntry _skillEntryHolder;

	public i_call_skill_by_target(EffectTemplate template)
	{
		super(template);
		int skillId = getParams().getInteger("id");
		int skillLevel = getParams().getInteger("level", 1);
		_skillEntryHolder = SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLevel);
	}

	public SkillEntry getSkillEntry()
	{
		return _skillEntryHolder;
	}

	public void instantUse(Creature caster, Creature target, boolean reflected)
	{
		if(_skillEntryHolder == null)
		{ return; }

		SkillEntry tempSkillEntry = _skillEntryHolder;
		Skill skill = tempSkillEntry.getTemplate();
		Creature aimTarget = skill.getAimingTarget(caster, target);
		if(aimTarget != null)
		{
			callSkill(caster, target, tempSkillEntry, false, true);
		}
	}

	/**
	 * callSkill TODO use Creature instead
	 * @param caster
	 * @param target
	 * @param skillEntry
	 * @param showAnimation
	 * @param checkConditions
	 */
	public static void callSkill(Creature caster, Creature target, SkillEntry skillEntry, boolean showAnimation, boolean checkConditions)
	{
		final Set<Creature> ss = new HashSet<>();
		if(!checkConditions || skillEntry.checkCondition(caster, target, true, true, true, false, false))
		{
			final Skill skill = skillEntry.getTemplate();
			if(showAnimation && !skill.isNotBroadcastable() && !caster.isCastingNow())
			{
				caster.broadcastPacket(new MagicSkillUse(caster, target, skillEntry.getDisplayId(), skillEntry.getDisplayLevel(), 0, 0));
			}

			ss.add(target);
			caster.callSkill(target, skillEntry, ss, false, skill.isTrigger());
			caster.disableSkill(skill, skill.getReuseDelay());
		}
	}
}