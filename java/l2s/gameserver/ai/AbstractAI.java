package l2s.gameserver.ai;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.lang.reference.HardReference;
import l2s.commons.lang.reference.HardReferences;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.skills.SkillEntry;

public abstract class AbstractAI implements Runnable
{
	protected static final Logger _log = LoggerFactory.getLogger(AbstractAI.class);

	protected final Creature _actor;
	private HardReference<? extends Creature> _attackTarget = HardReferences.emptyRef();
	private HardReference<? extends Creature> _castTarget = HardReferences.emptyRef();
	private CtrlIntention _intention = CtrlIntention.AI_INTENTION_IDLE;

	protected AbstractAI(Creature actor)
	{
		_actor = actor;
	}

	public void changeIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		_intention = intention;
		if (intention != CtrlIntention.AI_INTENTION_CAST && intention != CtrlIntention.AI_INTENTION_ATTACK)
		{
			setAttackTarget(null);
		}
	}

	public final void setIntention(CtrlIntention intention)
	{
		setIntention(intention, null, null);
	}

	public final void setIntention(CtrlIntention intention, Object arg0)
	{
		setIntention(intention, arg0, null);
	}

	public void setIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		final Creature actor = getActor();

		if (intention != CtrlIntention.AI_INTENTION_CAST && intention != CtrlIntention.AI_INTENTION_ATTACK)
		{
			setAttackTarget(null);
		}

		if (!actor.isVisible())
		{
			if (_intention == CtrlIntention.AI_INTENTION_IDLE)
			{
				return;
			}
			intention = CtrlIntention.AI_INTENTION_IDLE;
		}

		actor.getListeners().onAiIntention(intention, arg0, arg1);

		switch (intention)
		{
			case AI_INTENTION_IDLE:
				onIntentionIdle();
				break;
			case AI_INTENTION_ACTIVE:
				onIntentionActive();
				break;
			case AI_INTENTION_REST:
				onIntentionRest();
				break;
			case AI_INTENTION_ATTACK:
				onIntentionAttack((Creature) arg0);
				break;
			case AI_INTENTION_CAST:
				onIntentionCast((SkillEntry) arg0, (Creature) arg1);
				break;
			case AI_INTENTION_PICK_UP:
				onIntentionPickUp((GameObject) arg0);
				break;
			case AI_INTENTION_INTERACT:
				onIntentionInteract((GameObject) arg0);
				break;
			case AI_INTENTION_FOLLOW:
				onIntentionFollow((Creature) arg0, (Integer) arg1);
				break;
			case AI_INTENTION_COUPLE_ACTION:
				onIntentionCoupleAction((Player) arg0, (Integer) arg1);
				break;
			case AI_INTENTION_RETURN_HOME:
				onIntentionReturnHome((Boolean) arg0);
				break;
			case AI_INTENTION_WALKER_ROUTE:
				onIntentionWalkerRoute();
				break;
		}
	}

	public final void notifyEvent(CtrlEvent evt)
	{
		notifyEvent(evt, ArrayUtils.EMPTY_OBJECT_ARRAY);
	}

	public final void notifyEvent(CtrlEvent evt, Object arg0)
	{
		notifyEvent(evt, new Object[]
		{
			arg0
		});
	}

	public final void notifyEvent(CtrlEvent evt, Object arg0, Object arg1)
	{
		notifyEvent(evt, new Object[]
		{
			arg0,
			arg1
		});
	}

	public final void notifyEvent(CtrlEvent evt, Object arg0, Object arg1, Object arg2)
	{
		notifyEvent(evt, new Object[]
		{
			arg0,
			arg1,
			arg2
		});
	}

	public void notifyEvent(CtrlEvent evt, Object[] args)
	{
		final Creature actor = getActor();
		if (actor == null || !actor.isVisible())
		{
			return;
		}

		actor.getListeners().onAiEvent(evt, args);

		switch (evt)
		{
			case EVT_THINK:
				onEvtThink();
				break;
			case EVT_ATTACK:
				onEvtAttack((Creature) args[0], (Skill) args[1], ((Number) args[2]).intValue());
				break;
			case EVT_ATTACKED:
				onEvtAttacked((Creature) args[0], (Skill) args[1], ((Number) args[2]).intValue());
				break;
			case EVT_CLAN_ATTACKED:
				onEvtClanAttacked((NpcInstance) args[0], (Creature) args[1], ((Number) args[2]).intValue());
				break;
			case EVT_CLAN_DIED:
				onEvtClanDied((NpcInstance) args[0], (Creature) args[1]);
				break;
			case EVT_PARTY_ATTACKED:
				onEvtPartyAttacked((NpcInstance) args[0], (Creature) args[1], ((Number) args[2]).intValue());
				break;
			case EVT_PARTY_DIED:
				onEvtPartyDied((NpcInstance) args[0], (Creature) args[1]);
				break;
			case EVT_AGGRESSION:
				onEvtAggression((Creature) args[0], ((Number) args[1]).intValue());
				break;
			case EVT_READY_TO_ACT:
				onEvtReadyToAct();
				break;
			case EVT_ARRIVED:
				onEvtArrived();
				break;
			case EVT_ARRIVED_TARGET:
				onEvtArrivedTarget();
				break;
			case EVT_ARRIVED_BLOCKED:
				onEvtArrivedBlocked((Location) args[0]);
				break;
			case EVT_FORGET_OBJECT:
				onEvtForgetObject((GameObject) args[0]);
				break;
			case EVT_DEAD:
				onEvtDead((Creature) args[0]);
				break;
			case EVT_FAKE_DEATH:
				onEvtFakeDeath();
				break;
			case EVT_FINISH_CASTING:
				onEvtFinishCasting((Skill) args[0], (Creature) args[1], (Boolean) args[2]);
				break;
			case EVT_SEE_SPELL:
				onEvtSeeSpell((Skill) args[0], (Creature) args[1], (Creature) args[2]);
				break;
			case EVT_SPAWN:
				onEvtSpawn();
				break;
			case EVT_DESPAWN:
				onEvtDeSpawn();
				break;
			case EVT_DELETE:
				onEvtDelete();
				break;
			case EVT_TIMER:
				onEvtTimer(((Number) args[0]).intValue(), args[1], args[2]);
				break;
			case EVT_SCRIPT_EVENT:
				onEvtScriptEvent(args[0].toString(), args[1], args[2]);
				break;
			case EVT_MENU_SELECTED:
				onEvtMenuSelected((Player) args[0], ((Number) args[1]).intValue(), ((Number) args[2]).intValue());
				break;
			case EVT_KNOCK_DOWN:
				onEvtKnockDown((Creature) args[0]);
				break;
			case EVT_KNOCK_BACK:
				onEvtKnockBack((Creature) args[0]);
				break;
			case EVT_FLY_UP:
				onEvtFlyUp((Creature) args[0]);
				break;
			case EVT_TELEPORTED:
				onEvtTeleported();
				break;
			case EVT_SEE_CREATURE:
				onEvtSeeCreatue((Creature) args[0]);
				break;
			case EVT_DISAPPEAR_CREATURE:
				onEvtDisappearCreatue((Creature) args[0]);
				break;
			case EVT_FINISH_WALKER_ROUTE:
				onEvtFinishWalkerRoute(((Number) args[0]).intValue());
				break;
			case EVT_MOST_HATED_CHANGED:
				onEvtMostHatedChanged();
				break;
			case EVT_NO_DESIRE:
				onEvtNoDesire();
				break;
		}
	}

	protected void clientActionFailed()
	{
		final Creature actor = getActor();
		if (actor != null && actor.isPlayer())
		{
			actor.sendActionFailed();
		}
	}

	/**
	 * Останавливает движение
	 */
	public void clientStopMoving()
	{
		final Creature actor = getActor();
		actor.getMovement().stopMove();
	}

	public Creature getActor()
	{
		return _actor;
	}

	public CtrlIntention getIntention()
	{
		return _intention;
	}

	public void setAttackTarget(Creature target)
	{
		_attackTarget = target == null ? HardReferences.<Creature>emptyRef() : target.getRef();
	}

	public Creature getAttackTarget()
	{
		return _attackTarget.get();
	}

	public void setCastTarget(Creature target)
	{
		_castTarget = target == null ? HardReferences.<Creature>emptyRef() : target.getRef();
	}

	public Creature getCastTarget()
	{
		return _castTarget.get();
	}

	/** Означает, что AI всегда включен, независимо от состояния региона */
	public boolean isGlobalAI()
	{
		return false;
	}

	@Override
	public void run()
	{
		//
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " for " + getActor();
	}

	protected abstract void onIntentionIdle();

	protected abstract void onIntentionActive();

	protected abstract void onIntentionRest();

	protected abstract void onIntentionAttack(Creature target);

	protected abstract void onIntentionCast(SkillEntry skillEntry, Creature target);

	protected abstract void onIntentionPickUp(GameObject item);

	protected abstract void onIntentionInteract(GameObject object);

	protected abstract void onIntentionCoupleAction(Player player, Integer socialId);

	protected abstract void onIntentionReturnHome(boolean running);

	protected abstract void onEvtThink();

	protected abstract void onEvtAttack(Creature attacker, Skill skill, int damage);

	protected abstract void onEvtAttacked(Creature attacker, Skill skill, int damage);

	protected abstract void onEvtClanAttacked(NpcInstance member, Creature attacker, int damage);

	protected abstract void onEvtClanDied(NpcInstance member, Creature killer);

	protected abstract void onEvtPartyAttacked(NpcInstance minion, Creature attacker, int damage);

	protected abstract void onEvtPartyDied(NpcInstance minion, Creature killer);

	protected abstract void onEvtAggression(Creature target, int aggro);

	protected abstract void onEvtReadyToAct();

	protected abstract void onEvtArrived();

	protected abstract void onEvtArrivedTarget();

	protected abstract void onEvtTeleported();

	protected abstract void onEvtArrivedBlocked(Location blocked_at_pos);

	protected abstract void onEvtForgetObject(GameObject object);

	protected abstract void onEvtDead(Creature killer);

	protected abstract void onEvtFakeDeath();

	protected abstract void onEvtFinishCasting(Skill skill, Creature target, boolean success);

	protected abstract void onEvtSeeSpell(Skill skill, Creature caster, Creature target);

	protected abstract void onEvtSpawn();

	protected abstract void onEvtDeSpawn();

	protected abstract void onEvtDelete();

	protected abstract void onIntentionFollow(Creature target, Integer offset);

	protected abstract void onEvtTimer(int timerId, Object arg1, Object arg2);

	protected abstract void onEvtScriptEvent(String event, Object arg1, Object arg2);

	protected abstract void onEvtMenuSelected(Player player, int ask, int reply);

	protected abstract void onEvtKnockDown(Creature arg1);

	protected abstract void onEvtKnockBack(Creature arg1);

	protected abstract void onEvtFlyUp(Creature arg1);

	protected abstract void onEvtSeeCreatue(Creature creature);

	protected abstract void onEvtDisappearCreatue(Creature creature);

	protected abstract void onIntentionWalkerRoute();

	protected void onEvtFinishWalkerRoute(int routeId)
	{
		//
	}

	protected void onEvtMostHatedChanged()
	{
		//
	}

	protected abstract void onEvtNoDesire();

	public boolean canAttackCharacter(Creature target)
	{
		return false;
	}
}