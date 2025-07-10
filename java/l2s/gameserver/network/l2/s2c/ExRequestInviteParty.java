package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.network.l2.components.ChatType;

public class ExRequestInviteParty extends L2GameServerPacket
{
	private final int reqType;
	private final ChatType sayType;
	private final int charRankGrade;
	private final int pledgeCastleDBID;
	private final int userSID;

	private String name;

	public ExRequestInviteParty(Player player, int reqType, ChatType sayType)
	{
		name = player.getName();

		int rank = RankManager.getInstance().getPlayerGlobalRank(player.getObjectId());
		if (rank == 1)
		{
			charRankGrade = 1;
		}
		else if (rank <= 30)
		{
			charRankGrade = 2;
		}
		else if (rank <= 100)
		{
			charRankGrade = 3;
		}
		else
		{
			charRankGrade = 0;
		}
		Castle castle = player.getCastle();
		pledgeCastleDBID = castle == null ? 0 : castle.getId();
		userSID = player.getObjectId();

		this.reqType = reqType;
		this.sayType = sayType;
	}

	public void setName(String sName)
	{
		this.name = sName;
	}

	@Override
	protected final void writeImpl()
	{
		writeString(name);
		writeC(reqType);
		writeC(sayType.ordinal());
		writeC(charRankGrade);
		writeC(pledgeCastleDBID);
		writeD(userSID);
	}
}