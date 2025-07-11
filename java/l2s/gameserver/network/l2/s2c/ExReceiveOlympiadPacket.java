package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.entity.olympiad.CompType;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.model.entity.olympiad.OlympiadGame;
import l2s.gameserver.model.entity.olympiad.OlympiadManager;
import l2s.gameserver.model.entity.olympiad.OlympiadMember;
import l2s.gameserver.network.l2.ServerPacketOpcodes;

/**
 * @author VISTALL
 * @date 0:50/09.04.2011
 */
public abstract class ExReceiveOlympiadPacket implements IClientOutgoingPacket
{
	public static class MatchList extends ExReceiveOlympiadPacket
	{
		private List<ArenaInfo> _arenaList = Collections.emptyList();

		public MatchList()
		{
			super(0);
			OlympiadManager manager = Olympiad._manager;
			if (manager != null)
			{
				_arenaList = new ArrayList<>();
				for (OlympiadGame game : manager.getGames())
				{
					if (game.getState() > OlympiadGame.NONE_STATE)
					{
						_arenaList.add(new ArenaInfo(game.getId(), game.getState(), game.getType().ordinal(), game.getTeamName1(), game.getTeamName2()));
					}
				}
			}
		}

		public MatchList(List<ArenaInfo> arenaList)
		{
			super(0);
			_arenaList = arenaList;
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			super.writeImpl();
			packetWriter.writeD(_arenaList.size());
			packetWriter.writeD(0x00); // unknown
			for (ArenaInfo arena : _arenaList)
			{
				packetWriter.writeD(arena._id);
				packetWriter.writeD(arena._matchType);
				packetWriter.writeD(arena._status);
				packetWriter.writeS(arena._name1);
				packetWriter.writeS(arena._name2);
			}
			return true;
		}

		public static class ArenaInfo
		{
			public int _status;
			private int _id, _matchType;
			public String _name1, _name2;

			public ArenaInfo(int id, int status, int match_type, String name1, String name2)
			{
				_id = id;
				_status = status;
				_matchType = match_type;
				_name1 = name1;
				_name2 = name2;
			}
		}
	}

	public static class MatchResult extends ExReceiveOlympiadPacket
	{
		private final TeamType winnerTeam;
		private final String winnerTopDamagerName;
		private final List<PlayerInfo> teamOne = new ArrayList<>(3);
		private final List<PlayerInfo> teamTwo = new ArrayList<>(3);

		public MatchResult(TeamType winnerTeam, CompType matchType, String winnerTopDamagerName)
		{
			super(matchType == CompType.TEAM ? 3 : 1);
			this.winnerTeam = winnerTeam;
			this.winnerTopDamagerName = winnerTopDamagerName;
		}

		public void addPlayer(OlympiadMember member, int gameResultPoints, int gameId)
		{
			addPlayer(member.getTeam(), member.getName(), member.getClanName(), member.getClanId(), member.getClassId(), (int) member.getDealedDamage(), member.getStat().getPoints(), gameResultPoints, gameId);
		}

		public void addPlayer(TeamType team, String name, String clanName, int clanId, int classId, int damage, int points, int resultPoints, int arenaIndex)
		{
			if ((team == winnerTeam) || ((winnerTeam == TeamType.NONE) && (team == TeamType.BLUE)))
			{
				teamOne.add(new PlayerInfo(name, clanName, clanId, classId, damage, points, resultPoints, arenaIndex));
			}
			else if ((team == winnerTeam.revert()) || ((winnerTeam == TeamType.NONE) && (team == TeamType.RED)))
			{
				teamTwo.add(new PlayerInfo(name, clanName, clanId, classId, damage, points, resultPoints, arenaIndex));
			}
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			super.writeImpl();
			packetWriter.writeD(winnerTeam == TeamType.NONE);
			packetWriter.writeS(winnerTopDamagerName);
			packetWriter.writeD(1); // Team type
			packetWriter.writeD(teamOne.size());
			teamOne.forEach(p ->
			{
				packetWriter.writeS(p.name);
				packetWriter.writeS(p.clanName);
				packetWriter.writeD(p.pledgeId);
				packetWriter.writeD(p.classId);
				packetWriter.writeD(p.damage);
				packetWriter.writeD(p.currentPoints);
				packetWriter.writeD(p.gamePoints);
				packetWriter.writeD(p.arenaIndex);
			});
			packetWriter.writeD(2); // Team type
			packetWriter.writeD(teamTwo.size());
			teamTwo.forEach(p ->
			{
				packetWriter.writeS(p.name);
				packetWriter.writeS(p.clanName);
				packetWriter.writeD(p.pledgeId);
				packetWriter.writeD(p.classId);
				packetWriter.writeD(p.damage);
				packetWriter.writeD(p.currentPoints);
				packetWriter.writeD(p.gamePoints);
				packetWriter.writeD(p.arenaIndex);
			});
			return true;
		}

		private static class PlayerInfo
		{
			private final String name, clanName;
			private final int pledgeId, classId, damage, currentPoints, gamePoints, arenaIndex;

			public PlayerInfo(String name, String clanName, int pledgeId, int classId, int damage, int currentPoints, int gamePoints, int arenaIndex)
			{
				this.name = name;
				this.clanName = clanName;
				this.pledgeId = pledgeId;
				this.classId = classId;
				this.damage = damage;
				this.currentPoints = currentPoints;
				this.gamePoints = gamePoints;
				this.arenaIndex = arenaIndex;
			}
		}
	}

	private int _type;

	public ExReceiveOlympiadPacket(int type)
	{
		_type = type;
	}

	@Override
	protected ServerPacketOpcodes getOpcodes()
	{
		return ServerPacketOpcodes.ExReceiveOlympiadPacket;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_type);
		return true;
	}
}
