package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.utils.GameStringUtils;
import l2s.gameserver.utils.Util;

public class RequestGiveNickName implements IClientIncomingPacket
{
	private String _target;
	private String _title;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_target = packet.readS(Config.CNAME_MAXLEN);
		_title = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		_title = GameStringUtils.checkTitle(_title, Integer.MAX_VALUE, true);

		if(!_title.isEmpty() && !Util.isMatchingRegexp(_title, Config.CLAN_TITLE_TEMPLATE))
		{
			activeChar.sendMessage("Incorrect title.");
			return;
		}

		// Can the player change/give a title?
		else if((activeChar.getClanPrivileges() & Clan.CP_CL_MANAGE_TITLES) != Clan.CP_CL_MANAGE_TITLES)
			return;

		if(activeChar.getClan().getLevel() < 3)
		{
			activeChar.sendPacket(SystemMsg.A_PLAYER_CAN_ONLY_BE_GRANTED_A_TITLE_IF_THE_CLAN_IS_LEVEL_3_OR_ABOVE);
			return;
		}

		UnitMember member = activeChar.getClan().getAnyMember(_target);
		if(member != null)
		{
			member.setTitle(_title);
			if(member.isOnline())
			{
				member.getPlayer().sendPacket(SystemMsg.YOUR_TITLE_HAS_BEEN_CHANGED);
				member.getPlayer().sendChanges();
			}
		}
		else
			activeChar.sendMessage(new CustomMessage("l2s.gameserver.network.l2.c2s.RequestGiveNickName.NotInClan"));

	}
}