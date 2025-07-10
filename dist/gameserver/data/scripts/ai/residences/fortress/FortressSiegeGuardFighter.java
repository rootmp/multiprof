package ai.residences.fortress;

import java.util.List;

import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.events.impl.FortressSiegeEvent;
import l2s.gameserver.model.entity.events.objects.ZoneObject;
import l2s.gameserver.model.instances.NpcInstance;

import ai.residences.SiegeGuardFighter;

public class FortressSiegeGuardFighter extends SiegeGuardFighter
{
	private final int attackNpcId;

	private NpcInstance flagNpc = null;

	public FortressSiegeGuardFighter(NpcInstance actor)
	{
		super(actor);
		attackNpcId = actor.getParameter("attack_npc_id", 0);
		if (attackNpcId > 0)
		{
			setMaxPursueRange(8000);
		}
	}

	@Override
	public boolean isGlobalAI()
	{
		return attackNpcId > 0 || super.isGlobalAI();
	}

	@Override
	protected boolean thinkActive()
	{
		if (super.thinkActive())
			return true;

		if (attackNpcId > 0)
		{
			NpcInstance flagNpc = getFlagNpc();
			if (flagNpc != null)
			{
				if (getActor().getAggroList().getHate(flagNpc) <= 0)
				{
					notifyEvent(CtrlEvent.EVT_AGGRESSION, flagNpc, 10);
					return true;
				}
			}
		}
		return false;
	}

	private NpcInstance getFlagNpc()
	{
		if (flagNpc == null)
		{
			FortressSiegeEvent event = getActor().getEvent(FortressSiegeEvent.class);
			if (event != null)
			{
				List<ZoneObject> zoneObjects = event.getObjects(FortressSiegeEvent.SIEGE_ZONES, ZoneObject.class);
				for (ZoneObject zoneObject : zoneObjects)
				{
					Zone zone = zoneObject.getZone();
					for (NpcInstance npc : zone.getInsideNpcs())
					{
						if (npc.getNpcId() == attackNpcId)
						{
							if (npc.containsEvent(event))
							{
								flagNpc = npc;
							}
						}
					}
				}
			}
		}
		return flagNpc;
	}

	@Override
	public boolean canAttackCharacter(Creature target)
	{
		if (attackNpcId > 0 && target.getNpcId() == attackNpcId)
		{
			FortressSiegeEvent event = getActor().getEvent(FortressSiegeEvent.class);
			if (event != null && target.containsEvent(event))
			{
				return true;
			}
		}
		return super.canAttackCharacter(target);
	}
}
