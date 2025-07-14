/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.gameserver.model.entity.events.impl.fightclub;

import java.util.Collection;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubPlayer;
import l2s.gameserver.model.entity.events.fightclubmanager.enums.EventState;
import l2s.gameserver.model.entity.events.fightclubmanager.enums.MessageType;
import l2s.gameserver.model.entity.events.impl.AbstractFightClub;
import l2s.gameserver.model.instances.NpcInstance;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class FFATreasureHuntEvent extends AbstractFightClub
{
	private static final int CHEST_ID = 37061;
	private final double badgesOpenChest;
	private final int scoreForKilledPlayer;
	private final int scoreForChest;
	private final long timeForRespawningChest;
	private final int numberOfChests;
	private final Collection<NpcInstance> spawnedChests;

	public FFATreasureHuntEvent(MultiValueSet<String> set)
	{
		super(set);
		badgesOpenChest = set.getDouble("badgesOpenChest");
		scoreForKilledPlayer = set.getInteger("scoreForKilledPlayer");
		scoreForChest = set.getInteger("scoreForChest");
		timeForRespawningChest = set.getLong("timeForRespawningChest");
		numberOfChests = set.getInteger("numberOfChests");
		spawnedChests = new java.util.concurrent.CopyOnWriteArrayList<NpcInstance>();
	}

	public void onKilled(Creature actor, Creature victim)
	{
		if((actor != null) && (actor.isPlayable()))
		{
			FightClubPlayer realActor = getFightClubPlayer(actor.getPlayer());
			if(realActor != null)
			{
				if(victim.isPlayer())
				{
					realActor.increaseKills(true);
					realActor.increaseScore(scoreForKilledPlayer);
					updatePlayerScore(realActor);
					sendMessageToPlayer(realActor, MessageType.GM, "You have killed " + victim.getName());
				}
				actor.getPlayer().sendUserInfo();
			}
		}

		if(victim.isPlayer())
		{
			FightClubPlayer realVictim = getFightClubPlayer(victim);
			if(realVictim != null)
			{
				realVictim.increaseDeaths();
				if(actor != null)
					sendMessageToPlayer(realVictim, MessageType.GM, "You have been killed by " + actor.getName());
				victim.getPlayer().sendUserInfo();
			}
		}

		super.onKilled(actor, victim);
	}

	private void spawnChest()
	{
		spawnedChests.add(chooseLocAndSpawnNpc(CHEST_ID, getMap().getKeyLocations(), 0));
	}

	public void startRound()
	{
		super.startRound();

		for(int i = 0; i < numberOfChests; i++)
		{
			spawnChest();
		}
	}

	@Override
	public void stopEvent(boolean force)
	{
		super.stopEvent(force);

		for(NpcInstance chest : spawnedChests)
			if((chest != null) && (!chest.isDead()))
				chest.deleteMe();
		spawnedChests.clear();
	}

	public boolean openTreasure(Player player, NpcInstance npc)
	{
		FightClubPlayer fPlayer = getFightClubPlayer(player);
		if(fPlayer == null)
		{ return false; }

		if(getState() != EventState.STARTED)
		{ return false; }
		fPlayer.increaseEventSpecificScore("chest");
		fPlayer.increaseScore(scoreForChest);
		updatePlayerScore(fPlayer);
		player.sendUserInfo();

		ThreadPoolManager.getInstance().schedule(new SpawnChest(this), timeForRespawningChest * 1000L);

		spawnedChests.remove(npc);

		return true;
	}

	private static class SpawnChest implements Runnable
	{
		private final FFATreasureHuntEvent _event;

		private SpawnChest(FFATreasureHuntEvent event)
		{
			_event = event;
		}

		public void run()
		{
			if(_event.getState() != EventState.NOT_ACTIVE)
			{
				_event.spawnChest();
			}
		}
	}

	protected int getBadgesEarned(FightClubPlayer fPlayer, int currentValue, boolean isTopKiller)
	{
		int newValue = currentValue + addMultipleBadgeToPlayer(fPlayer.getEventSpecificScore("chest"), badgesOpenChest);
		return super.getBadgesEarned(fPlayer, newValue, isTopKiller);
	}

	public String getVisibleTitle(Player player, String currentTitle, boolean toMe)
	{
		FightClubPlayer fPlayer = getFightClubPlayer(player);

		if(fPlayer == null)
		{ return currentTitle; }
		return "Chests: " + fPlayer.getEventSpecificScore("chest") + " Kills: " + fPlayer.getKills(true);
	}
}
