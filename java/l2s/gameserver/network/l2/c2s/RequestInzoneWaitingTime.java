package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExInzoneWaitingInfo;

/**
 * @author Bonux
 **/
public class RequestInzoneWaitingTime extends L2GameClientPacket
{
	private boolean _openWindow;

	@Override
	protected boolean readImpl()
	{
		_openWindow = readC() > 0;
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExInzoneWaitingInfo(activeChar, _openWindow));
	}
}