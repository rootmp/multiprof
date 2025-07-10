package l2s.gameserver.network.l2.c2s.events;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.events.ExFestivalBMAllItemInfo;
import l2s.gameserver.network.l2.s2c.events.ExFestivalBMInfo;

/**
 * @author nexvill
 */
public class RequestExFestivalBMInfo extends L2GameClientPacket
{
	private int _openWindow;

	@Override
	protected boolean readImpl()
	{
		_openWindow = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (_openWindow == 1)
		{
			activeChar.sendPacket(new ExFestivalBMInfo(activeChar));
			activeChar.sendPacket(new ExFestivalBMAllItemInfo());
		}
	}
}