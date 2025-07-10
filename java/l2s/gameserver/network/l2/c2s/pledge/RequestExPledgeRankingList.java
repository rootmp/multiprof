package l2s.gameserver.network.l2.c2s.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeRankingList;

/**
 * @author nexvill
 */
public class RequestExPledgeRankingList implements IClientIncomingPacket
{
	private int _tabId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_tabId = packet.readC(); // top150 or my clan
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExPledgeRankingList(activeChar, _tabId));
	}
}