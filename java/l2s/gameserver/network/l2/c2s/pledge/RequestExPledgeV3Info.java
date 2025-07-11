package l2s.gameserver.network.l2.c2s.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.PledgeReceiveWarList;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeEnemyInfoList;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeV3Info;

/**
 * @author nexvill
 */
public class RequestExPledgeV3Info implements IClientIncomingPacket
{
	private int page;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		page = packet.readC(); // unk 0
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.getClan() == null)
			return;

		activeChar.sendPacket(new ExPledgeV3Info(activeChar.getClan().getPoints(), activeChar.getClan().getRank(), activeChar.getClan().getAnnounce(), activeChar.getClan().isShowAnnounceOnEnter()));
		activeChar.sendPacket(new PledgeReceiveWarList(activeChar.getClan(), page));
		activeChar.sendPacket(new ExPledgeEnemyInfoList(activeChar.getClan()));
	}
}