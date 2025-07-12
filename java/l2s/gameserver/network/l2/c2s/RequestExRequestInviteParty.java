package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExRequestInviteParty;
import l2s.gameserver.utils.ChatUtils;

public class RequestExRequestInviteParty implements IClientIncomingPacket
{
	private int _cReqType;
	private ChatType _cSayType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_cReqType = packet.readC();
		_cSayType = l2s.commons.lang.ArrayUtils.valid(ChatType.VALUES, packet.readC());
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		
		if(_cSayType == null)
			return;
		
		if(activeChar.isChatBlocked())
		{
			activeChar.sendPacket(SystemMsg.YOU_ARE_NOT_ALLOWED_TO_CHAT_WITH_A_CONTACT_WHILE_A_CHATTING_BLOCK_IS_IMPOSED);
			return;
		}
		
		switch (_cReqType)
		{
			case 0:
			{
				if (activeChar.isInParty())
				{
					if(activeChar.getParty().isLeader(activeChar) && activeChar.getParty().getCommandChannel() == null)
						_cReqType =1;//костыль??
					else
					return;
				}
				break;
			}
			case 1: //CC
			{
				final Party party = activeChar.getParty();
				if ((party == null) || !party.isLeader(activeChar) || (party.getCommandChannel() != null))
					return;
				break;
			}
			default:
				break;
		}
		
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(SystemMsg.S_4172);
			return;
		}
		
		if(_cSayType!=ChatType.ALL &&_cSayType!=ChatType.SHOUT && _cSayType!=ChatType.TRADE&& _cSayType!=ChatType.PARTY&& _cSayType!=ChatType.CLAN&& _cSayType!=ChatType.ALLIANCE)
			return;
		
		ExRequestInviteParty cs = new ExRequestInviteParty(activeChar, _cReqType, _cSayType.ordinal());
		switch(_cSayType)
		{
			case ALL:
				ChatUtils.sayInvParty(activeChar, cs);
				activeChar.sendPacket(cs);
				break;
			case SHOUT:
			case TRADE:
				ChatUtils.shoutInvParty(activeChar, cs);
				activeChar.sendPacket(cs);
				break;
			case PARTY:
				Party party = activeChar.getParty();
				if(party == null)
					return;
				party.broadCast(cs);
				break;
			case CLAN:
				Clan clan = activeChar.getClan();
				if(clan == null)
					return;
				clan.broadcastToOnlineMembers(cs);
				break;
			case ALLIANCE:
				Alliance alliance = activeChar.getAlliance();
				if(alliance == null)
					return;
				alliance.broadcastToOnlineMembers(cs);
				break;
			default:
				break;
		}
	}
}