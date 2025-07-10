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

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubPlayer;
import l2s.gameserver.model.entity.events.fightclubmanager.enums.MessageType;
import l2s.gameserver.model.entity.events.impl.AbstractFightClub;

/**
* @author Hl4p3x for L2Scripts Essence
*/
public class TeamVSTeamEvent extends AbstractFightClub
{
	public TeamVSTeamEvent(MultiValueSet<String> set)
	{
		super(set);
	}

	public void onKilled(Creature actor, Creature victim)
	{
		if (actor != null && actor.isPlayable())
		{
			FightClubPlayer realActor = getFightClubPlayer(actor.getPlayer());
			if (victim.isPlayer() && realActor != null)
			{
				realActor.increaseKills(true);
				realActor.getTeam().incScore(1);
				updatePlayerScore(realActor);
				updateScreenScores();
				sendMessageToPlayer(realActor, MessageType.GM, "You have killed " + victim.getName());
			}
			// else if(!victim.isPet()); ???

			actor.getPlayer().sendUserInfo();
		}

		if (victim.isPlayer())
		{
			FightClubPlayer realVictim = getFightClubPlayer(victim);
			if (realVictim != null)
			{
				realVictim.increaseDeaths();
				if (actor != null)
					sendMessageToPlayer(realVictim, MessageType.GM, "You have been killed by " + actor.getName());
			}
			victim.broadcastCharInfo();
		}

		super.onKilled(actor, victim);
	}

	public String getVisibleTitle(Player player, String currentTitle, boolean toMe)
	{
		FightClubPlayer fPlayer = getFightClubPlayer(player);

		if (fPlayer == null)
			return currentTitle;

		return "Kills: " + fPlayer.getKills(true) + " Deaths: " + fPlayer.getDeaths();
	}
}