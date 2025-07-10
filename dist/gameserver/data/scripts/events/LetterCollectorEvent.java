package events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.util.Rnd;
import l2s.gameserver.Announcements;
import l2s.gameserver.listener.actor.OnActorAct;
import l2s.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.entity.events.objects.ItemObject;
import l2s.gameserver.model.entity.events.objects.RewardObject;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.network.l2.s2c.ExLetterCollectorUiLauncher;
import l2s.gameserver.utils.ItemFunctions;

public class LetterCollectorEvent extends FunEvent
{
	private class EventListeners implements OnActorAct, OnPlayerEnterListener
	{
		@Override
		public void onPlayerEnter(Player player)
		{
			player.sendPacket(new ExLetterCollectorUiLauncher(true, minLevel));
		}

		@Override
		public void onAct(Creature actor, String act, Object... args)
		{
			if (!act.equalsIgnoreCase(OnActorAct.EX_LETTER_COLLECTOR_TAKE_REWARD))
				return;

			if (!actor.isPlayer())
				return;

			Player player = actor.getPlayer();
			if (player.isActionsDisabled())
			{
				// TODO: Add msg.
				return;
			}

			if (player.isInStoreMode())
			{
				// TODO: Add msg.
				return;
			}

			if (player.isInTrade())
			{
				// TODO: Add msg.
				return;
			}

			if (player.isFishing())
			{
				// TODO: Add msg.
				return;
			}

			if (player.isInTrainingCamp())
			{
				// TODO: Add msg.
				return;
			}

			int setId;
			try
			{
				setId = (int) args[0];
			}
			catch (Exception e)
			{
				// TODO: Add msg.
				return;
			}

			List<ItemObject> lettersSet = getObjects("letters_set_" + setId, ItemObject.class);
			if (lettersSet.isEmpty())
			{
				// TODO: Add msg.
				return;
			}

			List<RewardObject> rewardsSet = getObjects("rewards_set_" + setId, RewardObject.class);
			if (rewardsSet.isEmpty())
			{
				// TODO: Add msg.
				return;
			}

			Map<Integer, Long> requiredItems = new HashMap<>();
			lettersSet.forEach(i -> requiredItems.put(i.getItemId(), requiredItems.getOrDefault(i.getItemId(), 0L) + 1));

			Inventory inventory = player.getInventory();
			inventory.writeLock();
			try
			{
				for (Map.Entry<Integer, Long> entry : requiredItems.entrySet())
				{
					if (!ItemFunctions.haveItem(player, entry.getKey(), entry.getValue()))
					{
						// TODO: Add msg.
						return;
					}
				}

				RewardObject rewardObject = null;

				double totalChance = rewardsSet.stream().mapToDouble(RewardObject::getChance).sum();
				if (totalChance >= 100 || Rnd.chance(totalChance))
				{
					List<Pair<RewardObject, Double>> rewards = new ArrayList<>();
					rewardsSet.forEach(r -> rewards.add(new Pair<>(r, r.getChance())));
					EnumeratedDistribution<RewardObject> distribution = new EnumeratedDistribution<>(rewards);
					rewardObject = distribution.sample();
				}

				if (rewardObject == null)
				{
					// TODO: Add msg.
					return;
				}

				lettersSet.forEach(i -> ItemFunctions.deleteItem(player, i.getItemId(), 1, true));
				ItemFunctions.addItem(player, rewardObject.getItemId(), Rnd.get(rewardObject.getMinCount(), rewardObject.getMaxCount()), true);
				player.sendPacket(new ExLetterCollectorUiLauncher(true, minLevel));

			}
			finally
			{
				inventory.writeUnlock();
			}
		}
	}

	private final int minLevel;
	private final EventListeners eventListeners = new EventListeners();

	public LetterCollectorEvent(MultiValueSet<String> set)
	{
		super(set);
		minLevel = set.getInteger("min_level", 1);
	}

	@Override
	public void startEvent()
	{
		super.startEvent();
		CharListenerList.addGlobal(eventListeners);
		Announcements.announceToAll(new ExLetterCollectorUiLauncher(true, minLevel));
	}

	@Override
	public void stopEvent(boolean force)
	{
		Announcements.announceToAll(new ExLetterCollectorUiLauncher(false, minLevel));
		CharListenerList.removeGlobal(eventListeners);
		super.stopEvent(force);
	}
}
