package l2s.gameserver.model.actor.listener;

import l2s.commons.listener.Listener;
import l2s.gameserver.listener.actor.player.OnBlessingItemListener;
import l2s.gameserver.listener.actor.player.OnClassChangeListener;
import l2s.gameserver.listener.actor.player.OnElementalLevelChangeListener;
import l2s.gameserver.listener.actor.player.OnEnchantItemListener;
import l2s.gameserver.listener.actor.player.OnExpReceiveListener;
import l2s.gameserver.listener.actor.player.OnFishingListener;
import l2s.gameserver.listener.actor.player.OnLearnCustomSkillListener;
import l2s.gameserver.listener.actor.player.OnLevelChangeListener;
import l2s.gameserver.listener.actor.player.OnOlympiadFinishBattleListener;
import l2s.gameserver.listener.actor.player.OnParticipateInCastleSiegeListener;
import l2s.gameserver.listener.actor.player.OnPickupItemListener;
import l2s.gameserver.listener.actor.player.OnPlayerChatMessageReceive;
import l2s.gameserver.listener.actor.player.OnPlayerClanInviteListener;
import l2s.gameserver.listener.actor.player.OnPlayerClanLeaveListener;
import l2s.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2s.gameserver.listener.actor.player.OnPlayerExitListener;
import l2s.gameserver.listener.actor.player.OnPlayerPartyInviteListener;
import l2s.gameserver.listener.actor.player.OnPlayerPartyLeaveListener;
import l2s.gameserver.listener.actor.player.OnPlayerSummonServitorListener;
import l2s.gameserver.listener.actor.player.OnPurgeRewardListener;
import l2s.gameserver.listener.actor.player.OnQuestFinishListener;
import l2s.gameserver.listener.actor.player.OnSocialActionListener;
import l2s.gameserver.listener.actor.player.OnTeleportListener;
import l2s.gameserver.listener.actor.player.OnTeleportedListener;
import l2s.gameserver.listener.actor.player.OnUseItemListener;
import l2s.gameserver.listener.actor.player.QuestionMarkListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.model.actor.instances.player.Elemental;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.RequestActionUse.Action;
import l2s.gameserver.network.l2.components.ChatType;

/**
 * @author G1ta0
 */
public class PlayerListenerList extends CharListenerList
{
	public PlayerListenerList(Player actor)
	{
		super(actor);
	}

	@Override
	public Player getActor()
	{
		return (Player) actor;
	}

	public void onEnter()
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPlayerEnterListener.class.isInstance(listener))
					((OnPlayerEnterListener) listener).onPlayerEnter(getActor());

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPlayerEnterListener.class.isInstance(listener))
					((OnPlayerEnterListener) listener).onPlayerEnter(getActor());
	}

	public void onExit()
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPlayerExitListener.class.isInstance(listener))
					((OnPlayerExitListener) listener).onPlayerExit(getActor());

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPlayerExitListener.class.isInstance(listener))
					((OnPlayerExitListener) listener).onPlayerExit(getActor());
	}

	public void onTeleport(int x, int y, int z, Reflection reflection)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnTeleportListener.class.isInstance(listener))
					((OnTeleportListener) listener).onTeleport(getActor(), x, y, z, reflection);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnTeleportListener.class.isInstance(listener))
					((OnTeleportListener) listener).onTeleport(getActor(), x, y, z, reflection);
	}

	public void onTeleported()
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnTeleportedListener.class.isInstance(listener))
					((OnTeleportedListener) listener).onTeleported(getActor());

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnTeleportedListener.class.isInstance(listener))
					((OnTeleportedListener) listener).onTeleported(getActor());
	}

	public void onPartyInvite()
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPlayerPartyInviteListener.class.isInstance(listener))
					((OnPlayerPartyInviteListener) listener).onPartyInvite(getActor());

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPlayerPartyInviteListener.class.isInstance(listener))
					((OnPlayerPartyInviteListener) listener).onPartyInvite(getActor());
	}

	public void onPartyLeave()
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPlayerPartyLeaveListener.class.isInstance(listener))
					((OnPlayerPartyLeaveListener) listener).onPartyLeave(getActor());

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPlayerPartyLeaveListener.class.isInstance(listener))
					((OnPlayerPartyLeaveListener) listener).onPartyLeave(getActor());
	}

	public void onClanInvite()
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPlayerClanInviteListener.class.isInstance(listener))
					((OnPlayerClanInviteListener) listener).onClanInvite(getActor());

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPlayerClanInviteListener.class.isInstance(listener))
					((OnPlayerClanInviteListener) listener).onClanInvite(getActor());
	}

	public void onClanLeave()
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPlayerClanLeaveListener.class.isInstance(listener))
					((OnPlayerClanLeaveListener) listener).onClanLeave(getActor());

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPlayerClanLeaveListener.class.isInstance(listener))
					((OnPlayerClanLeaveListener) listener).onClanLeave(getActor());
	}

	public void onSummonServitor(Servitor servitor)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPlayerSummonServitorListener.class.isInstance(listener))
					((OnPlayerSummonServitorListener) listener).onSummonServitor(getActor(), servitor);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPlayerSummonServitorListener.class.isInstance(listener))
					((OnPlayerSummonServitorListener) listener).onSummonServitor(getActor(), servitor);
	}

	/**
	 * Called when player do action
	 */
	public void onSocialAction(Action action)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnSocialActionListener.class.isInstance(listener))
					((OnSocialActionListener) listener).onSocialAction(getActor(), getActor().getTarget(), action);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnSocialActionListener.class.isInstance(listener))
					((OnSocialActionListener) listener).onSocialAction(getActor(), getActor().getTarget(), action);
	}

	public void onLevelChange(int oldLvl, int newLvl)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnLevelChangeListener.class.isInstance(listener))
					((OnLevelChangeListener) listener).onLevelChange(getActor(), oldLvl, newLvl);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnLevelChangeListener.class.isInstance(listener))
					((OnLevelChangeListener) listener).onLevelChange(getActor(), oldLvl, newLvl);
	}

	public void onElementalLevelChange(Elemental elemental, int oldLvl, int newLvl)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnElementalLevelChangeListener.class.isInstance(listener))
					((OnElementalLevelChangeListener) listener).onElementalLevelChange(getActor(), elemental, oldLvl, newLvl);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnElementalLevelChangeListener.class.isInstance(listener))
					((OnElementalLevelChangeListener) listener).onElementalLevelChange(getActor(), elemental, oldLvl, newLvl);
	}

	public void onClassChange(ClassId oldClass, ClassId newClass)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnClassChangeListener.class.isInstance(listener))
					((OnClassChangeListener) listener).onClassChange(getActor(), oldClass, newClass);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnClassChangeListener.class.isInstance(listener))
					((OnClassChangeListener) listener).onClassChange(getActor(), oldClass, newClass);
	}

	public void onPickupItem(ItemInstance item)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPickupItemListener.class.isInstance(listener))
					((OnPickupItemListener) listener).onPickupItem(getActor(), item);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPickupItemListener.class.isInstance(listener))
					((OnPickupItemListener) listener).onPickupItem(getActor(), item);
	}

	public void onEnchantItem(ItemInstance item, boolean success)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnEnchantItemListener.class.isInstance(listener))
					((OnEnchantItemListener) listener).onEnchantItem(getActor(), item, success);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnEnchantItemListener.class.isInstance(listener))
					((OnEnchantItemListener) listener).onEnchantItem(getActor(), item, success);
	}

	public void onBlessingItem(ItemInstance item, boolean success)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnBlessingItemListener.class.isInstance(listener))
					((OnBlessingItemListener) listener).onBlessingItem(getActor(), item, success);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnBlessingItemListener.class.isInstance(listener))
					((OnBlessingItemListener) listener).onBlessingItem(getActor(), item, success);
	}

	public void onPurgeReward(int zoneId)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPurgeRewardListener.class.isInstance(listener))
					((OnPurgeRewardListener) listener).onPurgeReward(getActor(), zoneId);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPurgeRewardListener.class.isInstance(listener))
					((OnPurgeRewardListener) listener).onPurgeReward(getActor(), zoneId);
	}

	public void onUseItem(ItemInstance item, boolean success)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnUseItemListener.class.isInstance(listener))
					((OnUseItemListener) listener).onUseItem(getActor(), item, success);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnUseItemListener.class.isInstance(listener))
					((OnUseItemListener) listener).onUseItem(getActor(), item, success);
	}

	public void onFishing(boolean success)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnFishingListener.class.isInstance(listener))
					((OnFishingListener) listener).onFishing(getActor(), success);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnFishingListener.class.isInstance(listener))
					((OnFishingListener) listener).onFishing(getActor(), success);
	}

	public void onOlympiadFinishBattle(boolean winner)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnOlympiadFinishBattleListener.class.isInstance(listener))
					((OnOlympiadFinishBattleListener) listener).onOlympiadFinishBattle(getActor(), winner);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnOlympiadFinishBattleListener.class.isInstance(listener))
					((OnOlympiadFinishBattleListener) listener).onOlympiadFinishBattle(getActor(), winner);
	}

	public void onQuestFinish(int questId)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnQuestFinishListener.class.isInstance(listener))
					((OnQuestFinishListener) listener).onQuestFinish(getActor(), questId);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnQuestFinishListener.class.isInstance(listener))
					((OnQuestFinishListener) listener).onQuestFinish(getActor(), questId);
	}

	public void onChatMessageReceive(ChatType type, String charName, String text)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnPlayerChatMessageReceive.class.isInstance(listener))
					((OnPlayerChatMessageReceive) listener).onChatMessageReceive(getActor(), type, charName, text);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnPlayerChatMessageReceive.class.isInstance(listener))
					((OnPlayerChatMessageReceive) listener).onChatMessageReceive(getActor(), type, charName, text);
	}

	public void onParticipateInCastleSiege(CastleSiegeEvent siegeEvent)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnParticipateInCastleSiegeListener.class.isInstance(listener))
					((OnParticipateInCastleSiegeListener) listener).onParticipateInCastleSiege(getActor(), siegeEvent);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnParticipateInCastleSiegeListener.class.isInstance(listener))
					((OnParticipateInCastleSiegeListener) listener).onParticipateInCastleSiege(getActor(), siegeEvent);
	}

	public void onLearnCustomSkill(SkillLearn skillLearn)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnLearnCustomSkillListener.class.isInstance(listener))
					((OnLearnCustomSkillListener) listener).onLearnCustomSkill(getActor(), skillLearn);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnLearnCustomSkillListener.class.isInstance(listener))
					((OnLearnCustomSkillListener) listener).onLearnCustomSkill(getActor(), skillLearn);
	}

	public void onExpReceive(long value, boolean hunting)
	{
		if (!global.getListeners().isEmpty())
			for (Listener<Creature> listener : global.getListeners())
				if (OnExpReceiveListener.class.isInstance(listener))
					((OnExpReceiveListener) listener).onExpReceive(getActor(), value, hunting);

		if (!getListeners().isEmpty())
			for (Listener<Creature> listener : getListeners())
				if (OnExpReceiveListener.class.isInstance(listener))
					((OnExpReceiveListener) listener).onExpReceive(getActor(), value, hunting);
	}

	public void onQuestionMarkClicked(int questionMarkId)
	{
		if (!global.getListeners().isEmpty())
			for (final Listener<Creature> listener : global.getListeners())
				if (QuestionMarkListener.class.isInstance(listener))
					((QuestionMarkListener) listener).onQuestionMarkClicked(getActor(), questionMarkId);

		if (!getListeners().isEmpty())
			for (final Listener<Creature> listener : getListeners())
				if (QuestionMarkListener.class.isInstance(listener))
					((QuestionMarkListener) listener).onQuestionMarkClicked(getActor(), questionMarkId);
	}
}
