package l2s.gameserver.skills.skillclasses;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.residences.SiegeFlagInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.templates.StatsSet;

/**
 * Created by Archer on 8/5/2014.
 */
public class Sacrifice extends Skill
{
	/**
	 * Внимание!!! У наследников вручную надо поменять тип на public
	 *
	 * @param set парамерты скилла
	 */
	public Sacrifice(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(SkillEntry skillEntry, Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first, boolean sendMsg, boolean trigger)
	{
		if (!super.checkCondition(skillEntry, activeChar, target, forceUse, dontMove, first, sendMsg, trigger))
			return false;

		if (target == null || target.isDoor() || target instanceof SiegeFlagInstance)
			return false;

		return true;
	}

	@Override
	protected void useSkill(Creature activeChar, Creature target, boolean reflected)
	{
		if (target.isHealBlocked())
			return;

		final double addToHp = Math.max(0, Math.min(getPower(), target.getStat().calc(Stats.HP_LIMIT, null, null) * target.getMaxHp() / 100. - target.getCurrentHp()));
		if (addToHp > 0)
		{
			target.setCurrentHp(addToHp + target.getCurrentHp(), false);

			if (getId() == 4051)
				target.sendPacket(SystemMsg.REJUVENATING_HP);
			else if (target.isPlayer())
			{
				if (activeChar == target)
					activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_HP_HAS_BEEN_RESTORED).addInteger(Math.round(addToHp)));
				else
					target.sendPacket(new SystemMessagePacket(SystemMsg.S2_HP_HAS_BEEN_RESTORED_BY_C1).addName(activeChar).addInteger(Math.round(addToHp)));
			}
		}
	}
}
