package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;

public class ExRequestInviteParty implements IClientOutgoingPacket
{
	private int _cReqType;
	private int _cSayType;
	private Player player;
	private boolean _is_cc = false;
	private final int cCharRankGrade;
	private final int cPledgeCastleDBID;

	public ExRequestInviteParty(Player activeChar, int cReqType, int cSayType)
	{
		player = activeChar;
		_cReqType = cReqType;
		_cSayType = cSayType;
		Party party = activeChar.getParty();
		if(party != null && party.isLeader(activeChar))
			_is_cc = true;

		cCharRankGrade = RankManager.getInstance().getPlayerGlobalRank(player);

		Castle castle = player.getCastle();
		cPledgeCastleDBID = castle == null ? 0 : castle.getId();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeSizedString(player.getName());//sName
		packetWriter.writeC(_cReqType);//cReqType
		packetWriter.writeC(_cSayType);//cSayType
		packetWriter.writeC(cCharRankGrade);//cCharRankGrade
		packetWriter.writeC(cPledgeCastleDBID);//cPledgeCastleDBID
		packetWriter.writeC(0);//cEventEmblemID

		packetWriter.writeD(0);//nChatBackground
		if(!_is_cc)
			packetWriter.writeC(player.getObjectId());//nUserSID
		return true;

	}
}