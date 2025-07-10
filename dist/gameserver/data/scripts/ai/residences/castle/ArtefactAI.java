package ai.residences.castle;

import l2s.commons.lang.reference.HardReference;
import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.NpcAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.entity.events.objects.SiegeClanObject;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillCastingType;
import l2s.gameserver.skills.enums.SkillTargetType;

/**
 * @author VISTALL
 * @date 8:32/06.04.2011
 */
public class ArtefactAI extends NpcAI
{
	public ArtefactAI(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
		NpcInstance actor;
		Player player;
		if (attacker == null || (player = attacker.getPlayer()) == null || (actor = getActor()) == null)
			return;

		SiegeEvent<?, ?> siegeEvent1 = actor.getEvent(SiegeEvent.class);
		SiegeEvent<?, ?> siegeEvent2 = player.getEvent(SiegeEvent.class);
		SiegeClanObject siegeClan = siegeEvent1.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());

		if (siegeEvent2 == null || siegeEvent1 == siegeEvent2 && siegeClan != null)
			ThreadPoolManager.getInstance().schedule(new notifyGuard(player), 1000);
	}

	class notifyGuard implements Runnable
	{
		private HardReference<Player> _playerRef;

		public notifyGuard(Player attacker)
		{
			_playerRef = attacker.getRef();
		}

		@Override
		public void run()
		{
			NpcInstance actor;
			Player attacker = _playerRef.get();
			if (attacker == null || (actor = getActor()) == null)
				return;

			for (NpcInstance npc : actor.getAroundNpc(1500, 200))
			{
				if (npc.isSiegeGuard() && Rnd.chance(20))
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
			}

			SkillEntry skillEntry = attacker.getSkillCast(SkillCastingType.NORMAL).getSkillEntry();
			if (skillEntry != null && skillEntry.getTemplate().getTargetType() == SkillTargetType.TARGET_HOLY)
				ThreadPoolManager.getInstance().schedule(this, 10000);
			else
			{
				skillEntry = attacker.getSkillCast(SkillCastingType.NORMAL_SECOND).getSkillEntry();
				if (skillEntry != null && skillEntry.getTemplate().getTargetType() == SkillTargetType.TARGET_HOLY)
					ThreadPoolManager.getInstance().schedule(this, 10000);
			}
		}
	}
}
