package npc.model.residences.castle;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.templates.npc.NpcTemplate;

public class CastleRelicInstance extends NpcInstance
{
	public CastleRelicInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		for (CastleSiegeEvent siegeEvent : getEvents(CastleSiegeEvent.class))
		{
			if (siegeEvent.isInProgress())
			{
				siegeEvent.spawnAction(CastleSiegeEvent.ARTEFACT, true);
				if (siegeEvent.getResidence().getId() == 3)
				{ // Giran
					siegeEvent.broadcastInZone(new ExShowScreenMessage(NpcString.GIRAN_HOLY_ARTIFACT_HAS_APPEARED, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, true));
				}
				else if (siegeEvent.getResidence().getId() == 7)
				{ // Goddard
					siegeEvent.broadcastInZone(new ExShowScreenMessage(NpcString.GODDARD_HOLY_ARTIFACT_HAS_APPEARED, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, true));
				}
			}
		}
		super.onDeath(killer);
	}

	@Override
	protected void onDecay()
	{
		super.onDecay();
		for (CastleSiegeEvent siegeEvent : getEvents(CastleSiegeEvent.class))
		{
			if (siegeEvent.isInProgress())
			{
				siegeEvent.spawnAction(CastleSiegeEvent.RELIC, false);
			}
		}
	}

	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		if (attacker == null)
			return false;

		Player player = attacker.getPlayer();
		if (player == null)
			return false;

		for (CastleSiegeEvent siegeEvent : getEvents(CastleSiegeEvent.class))
		{
			if (siegeEvent.isInProgress() && !siegeEvent.isParticipant(SiegeEvent.DEFENDERS, player))
				return true;
		}
		return false;
	}

	@Override
	public boolean isAttackable(Creature attacker)
	{
		return isAutoAttackable(attacker);
	}

	@Override
	public boolean isPeaceNpc()
	{
		return false;
	}

	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}

	@Override
	public boolean isFearImmune()
	{
		return true;
	}

	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}

	@Override
	public boolean isLethalImmune()
	{
		return true;
	}

	@Override
	public Clan getClan()
	{
		return null;
	}
}