package l2s.gameserver.network.l2.c2s.pvpbook;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpBookShareRevengeList;

/**
 * @author nexvill
 */
public class RequestExPvpBookShareRevengeList extends L2GameClientPacket
{
	private int _userId;

	@Override
	protected boolean readImpl()
	{
		_userId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExPvpBookShareRevengeList(activeChar));
	}
}