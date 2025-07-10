package ai;

import java.util.List;
import java.util.stream.Collectors;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.templates.StatsSet;

public interface ExtenderAI
{
	public static final StatsSet parameters = new StatsSet();

	default StatsSet getParameters()
	{
		return parameters;
	}

	default void setParameter(String key, Object value)
	{
		if (value == null)
		{
			parameters.remove(key);
		}
		else
		{
			parameters.set(key, value);
		}
	}

	default void setParameter(String key, boolean value)
	{
		parameters.set(key, value ? Boolean.TRUE : Boolean.FALSE);
	}

	default Player getRandomPlayer(NpcInstance actor)
	{
		return Rnd.get(getPlayersInside(actor));
	}

	default List<Player> getPlayersInside(NpcInstance actor)
	{
		Reflection reflection = actor.getReflection();
		if (!reflection.isMain())
			return reflection.getPlayers();
		return World.getAroundPlayers(actor);
	}

	default Creature getRandomCreature(NpcInstance actor, int dist)
	{
		return Rnd.get(getCreaturesInside(actor, dist));
	}

	default List<Creature> getCreaturesInside(NpcInstance actor, int dist)
	{
		return actor.getAroundCharacters(dist, 50).stream().filter(e -> (e.isPlayer() || e.isSummon())).collect(Collectors.toList());
	}

	default void forceAttack(NpcInstance minion, Player target)
	{
		if (target == null)
		{
			return;
		}

		minion.setTarget(target);
		minion.getAI().addTaskAttack(target, null, 1000000);
		minion.getAI().setAttackTarget(target);
		minion.getAI().setCastTarget(target);
		minion.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		minion.getAI().addAttackDesire(target, 1, 1000000L);
		minion.getAI().run();
		minion.getAI().startAITask();
		minion.getAI().Attack(target, true, false);
	}
}