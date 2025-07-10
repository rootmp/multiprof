package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExOlympiadRecord;

public class RequestExOlympiadUI extends L2GameClientPacket
{
	private int type;

	@Override
	protected boolean readImpl()
	{
		type = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExOlympiadRecord(activeChar));
	}
}
