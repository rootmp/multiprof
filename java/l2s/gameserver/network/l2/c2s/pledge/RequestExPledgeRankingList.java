package l2s.gameserver.network.l2.c2s.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeRankingList;

/**
 * @author nexvill
 */
public class RequestExPledgeRankingList extends L2GameClientPacket
{
	private int _tabId;

	@Override
	protected boolean readImpl()
	{
		_tabId = readC(); // top150 or my clan
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExPledgeRankingList(activeChar, _tabId));
	}
}