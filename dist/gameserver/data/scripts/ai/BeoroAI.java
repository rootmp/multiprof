package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.utils.NpcUtils;

public class BeoroAI extends Fighter
{
	private boolean transform = false;
	private boolean petmaster = false;

	public BeoroAI(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(Creature attacker, Skill skill, int damage)
	{
		if (!attacker.isPet())
			SkillEntry.makeSkillEntry(SkillEntryType.NONE, 48469, 1).getEffects(attacker, attacker);

		super.onEvtAttacked(attacker, skill, damage);

		if (getActor().getCurrentHpPercents() < 50 && getActor().getNpcId() == 25948)
		{
			if (!transform)
			{
				transform = true;
				if (Rnd.chance(30))
				{
					NpcUtils.spawnSingle(25949, getActor().getLoc(), 600000L);
					getActor().deleteMe();
				}
			}
		}
		else if (getActor().getCurrentHpPercents() < 25)
		{
			if (!petmaster)
			{
				petmaster = true;
				NpcUtils.spawnSingle(25950, getActor().getLoc(), 600000L);
			}
		}
	}
}
