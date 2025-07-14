package l2s.gameserver.skills.effects.instant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.skill.EffectTemplate;
import l2s.gameserver.utils.AbnormalsComparator;

public class i_dispel_by_skill extends i_abstract_effect
{
	private final int skillId;
	private final int minSkillLevel;

	public i_dispel_by_skill(EffectTemplate template)
	{
		super(template);
		skillId = getParams().getInteger("skill_id");
		minSkillLevel = getParams().getInteger("min_skill_level", -1);
	}

	@Override
	public void instantUse(Creature effector, Creature effected, boolean reflected)
	{
		final Creature target = effected;

		final List<Abnormal> abnormals = new ArrayList<>(target.getAbnormalList().values());
		abnormals.sort(AbnormalsComparator.getInstance()); // ToFix: Comparator to HF
		Collections.reverse(abnormals);

		for(Abnormal abnormal : abnormals)
		{
			Skill effectSkill = abnormal.getSkill();
			if(effectSkill == null)
				continue;

			if(effectSkill.getId() != skillId)
				continue;

			if(minSkillLevel > effectSkill.getLevel())
				continue;

			if(effectSkill.isToggle())
				continue;

			if(effectSkill.isPassive())
				continue;

			abnormal.exit();

			if(!abnormal.isHidden())
				target.sendPacket(new SystemMessagePacket(SystemMsg.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(effectSkill));
		}
	}
}