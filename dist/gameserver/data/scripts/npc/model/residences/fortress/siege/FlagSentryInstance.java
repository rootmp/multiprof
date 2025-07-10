package npc.model.residences.fortress.siege;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.entity.events.impl.FortressSiegeEvent;
import l2s.gameserver.templates.npc.NpcTemplate;

import npc.model.residences.SiegeGuardInstance;

public class FlagSentryInstance extends SiegeGuardInstance
{
	public FlagSentryInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		FortressSiegeEvent event = getEvent(FortressSiegeEvent.class);
		if (event != null)
		{
			event.dropFlag(this);
		}
		super.onDeath(killer);
	}
}
