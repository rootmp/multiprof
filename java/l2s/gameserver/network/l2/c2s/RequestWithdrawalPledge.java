package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import org.apache.commons.lang3.StringUtils;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.PledgeShowMemberListDeleteAllPacket;
import l2s.gameserver.network.l2.s2c.PledgeShowMemberListDeletePacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;

public class RequestWithdrawalPledge implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		// is the guy in a clan ?
		if (activeChar.getClanId() == 0)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isInCombat())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_LEAVE_A_CLAN_WHILE_ENGAGED_IN_COMBAT);
			return;
		}

		Clan clan = activeChar.getClan();
		if (clan == null)
			return;

		UnitMember member = clan.getAnyMember(activeChar.getObjectId());
		if (member == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (member.isClanLeader())
		{
			activeChar.sendMessage("A clan leader may not be dismissed.");
			return;
		}

		int subUnitType = activeChar.getPledgeType();

		clan.removeClanMember(subUnitType, activeChar.getObjectId());

		clan.broadcastToOnlineMembers(new SystemMessagePacket(SystemMsg.S1_HAS_WITHDRAWN_FROM_THE_CLAN).addString(activeChar.getName()), new PledgeShowMemberListDeletePacket(activeChar.getName()));

		if (subUnitType == Clan.SUBUNIT_ACADEMY)
			activeChar.setLvlJoinedAcademy(0);

		activeChar.setClan(null);
		activeChar.getListeners().onClanLeave();
		activeChar.setTitle(StringUtils.EMPTY);

		activeChar.setLeaveClanCurTime();
		activeChar.broadcastCharInfo();

		activeChar.sendPacket(SystemMsg.YOU_HAVE_RECENTLY_BEEN_DISMISSED_FROM_A_CLAN, PledgeShowMemberListDeleteAllPacket.STATIC);
	}
}