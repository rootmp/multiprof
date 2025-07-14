package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeBonusOpen;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeClassicRaidInfo;

/**
 * @author Bonux
 **/
public class RequestPledgeBonusOpen implements IClientIncomingPacket
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
		if(activeChar == null)
			return;

		if(activeChar.getClan() == null)
			return;

		activeChar.sendPacket(new ExPledgeBonusOpen(activeChar));
		activeChar.sendPacket(new ExPledgeClassicRaidInfo(activeChar));
	}
}