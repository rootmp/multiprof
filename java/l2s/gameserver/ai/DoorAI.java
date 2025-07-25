package l2s.gameserver.ai;

import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;

public class DoorAI extends CharacterAI
{
	public DoorAI(DoorInstance actor)
	{
		super(actor);
	}

	public void onEvtTwiceClick(Player player)
	{
		//
	}

	public void onEvtOpen(Player player)
	{
		//
	}

	public void onEvtClose(Player player)
	{
		//
	}

	@Override
	public DoorInstance getActor()
	{
		return (DoorInstance) super.getActor();
	}

	// TODO [VISTALL] унести в SiegeDoor
	@Override
	@SuppressWarnings("rawtypes")
	protected void onEvtAttacked(Creature attacker, Skill skill, int damage)
	{
		Creature actor;
		if(attacker == null || (actor = getActor()) == null)
			return;

		Player player = attacker.getPlayer();
		if(player == null)
			return;

		List<SiegeEvent> doorSiegeEvents = actor.getEvents(SiegeEvent.class);
		if(doorSiegeEvents.isEmpty())
			return;

		List<SiegeEvent> playerSiegeEvents = player.getEvents(SiegeEvent.class);

		boolean inSameSiege = false;
		if(playerSiegeEvents.isEmpty())
			inSameSiege = true;
		else
		{
			for(SiegeEvent<?, ?> event : playerSiegeEvents)
			{
				if(doorSiegeEvents.contains(event) && event.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan()) != null)
				{
					inSameSiege = true;
					break;
				}
			}
		}

		if(inSameSiege)
		{
			for(NpcInstance npc : actor.getAroundNpc(900, 200))
			{
				if(!npc.isSiegeGuard())
					continue;

				if(Rnd.chance(20))
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 10000);
				else
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 2000);
			}
		}
	}
}