package l2s.gameserver.network.l2.c2s.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeV3Info;

/**
 * Written by Eden, on 20.02.2021
 */
public class RequestExPledgeV3SetAnnounce implements IClientIncomingPacket
{
	private String announce;
	private boolean enterWorldShow;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		announce = readString();
		enterWorldShow = readC() == 1;
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		final Clan clan = activeChar.getClan();
		if (clan == null)
			return;

		clan.setAnnounce(announce);
		clan.setShowAnnounceOnEnter(enterWorldShow);
		clan.broadcastToOnlineMembers(new ExPledgeV3Info(clan.getPoints(), clan.getRank(), clan.getAnnounce(), clan.isShowAnnounceOnEnter()));
	}
}
