package ai.forgottenisland;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;

/**
 * @author Bonux
 **/
public class KetraOrcCaptain extends Fighter
{
	public KetraOrcCaptain(NpcInstance actor)
	{
		super(actor);
		actor.setAggroRange(300);
		actor.getFlags().getDeathImmunity().start(this);
	}

	@Override
	public boolean canAttackCharacter(Creature target)
	{
		return target.getNpcId() == 21760;
	}
}