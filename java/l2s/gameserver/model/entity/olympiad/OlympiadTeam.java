package l2s.gameserver.model.entity.olympiad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.TeamType;

public class OlympiadTeam
{
	private final TeamType team;
	private final OlympiadGame game;
	private final Map<Integer, OlympiadMember> members = new HashMap<>(1);
	private final String name;

	public OlympiadTeam(OlympiadParticipiantData[] participants, OlympiadGame game, TeamType team)
	{
		this.team = team;
		this.game = game;

		List<String> names = new ArrayList<>();
		for (int i = 0; i < participants.length; i++)
		{
			OlympiadParticipiantData p = participants[i];
			members.put(p.getObjectId(), new OlympiadMember(i, p, game, team));
			names.add(p.getName());
		}
		name = String.join("|", names);
	}

	public TeamType getTeam()
	{
		return team;
	}

	public String getName()
	{
		return name;
	}

	public Collection<OlympiadMember> getMembers()
	{
		return members.values();
	}

	public boolean isMember(int objectId)
	{
		return members.containsKey(objectId);
	}

	public void addDealedDamage(Player player, double damage)
	{
		OlympiadMember member = members.get(player.getObjectId());
		if (member != null)
			member.addDealedDamage(damage);
	}

	public double getDamage()
	{
		double damage = 0;
		for (OlympiadMember member : getMembers())
			damage += member.getDealedDamage();
		return damage;
	}

	public OlympiadMember getTopDamager()
	{
		OlympiadMember topDamager = null;
		for (OlympiadMember member : getMembers())
		{
			if (topDamager == null || topDamager.getDealedDamage() < member.getDealedDamage())
				topDamager = member;
		}
		return topDamager;
	}

	public void portPlayersToArena()
	{
		getMembers().forEach(OlympiadMember::portPlayerToArena);
	}

	public void preparePlayers1()
	{
		getMembers().forEach(OlympiadMember::preparePlayer1);
	}

	public void preparePlayers2()
	{
		getMembers().forEach(OlympiadMember::preparePlayer2);
	}

	public void portPlayersBack()
	{
		getMembers().forEach(OlympiadMember::portPlayerBack);
	}

	public void saveParticipantsData()
	{
		getMembers().forEach(OlympiadMember::saveParticipantData);
	}

	public boolean checkPlayers()
	{
		for (OlympiadMember member : getMembers())
		{
			if (member.checkPlayer())
				return true;
		}
		return false;
	}

	public boolean logout(Player player)
	{
		if (player != null)
		{
			for (OlympiadMember member : getMembers())
			{
				if (player.getObjectId() == member.getObjectId())
					member.logout();
			}
		}
		return checkPlayers();
	}

	public boolean doDie(Player player)
	{
		if (player != null)
		{
			for (OlympiadMember member : getMembers())
			{
				if (player.getObjectId() == member.getObjectId())
					member.doDie();
			}
		}
		for (OlympiadMember member : getMembers())
		{
			if (!member.isDead())
				return false;
		}
		return true;
	}

	public boolean isDead(Player player)
	{
		for (OlympiadMember member : getMembers())
		{
			if (player.getObjectId() == member.getObjectId() && member.isDead())
				return true;
		}
		return false;
	}
}
