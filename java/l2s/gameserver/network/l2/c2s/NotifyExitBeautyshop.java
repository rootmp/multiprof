package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class NotifyExitBeautyshop extends L2GameClientPacket
{
	@Override
	protected boolean readImpl() throws Exception
	{
		return true;
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.unblock();
		activeChar.broadcastCharInfo();
	}
}
