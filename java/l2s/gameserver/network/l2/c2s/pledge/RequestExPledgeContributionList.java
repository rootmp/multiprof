package l2s.gameserver.network.l2.c2s.pledge;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeContributionList;

/**
 * @author nexvill
 */
public class RequestExPledgeContributionList implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readC(); // unk 0
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

		activeChar.sendPacket(new ExPledgeContributionList(activeChar.getClan()));
	}
}