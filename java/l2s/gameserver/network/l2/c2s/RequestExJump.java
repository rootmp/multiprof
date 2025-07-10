package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;

public class RequestExJump extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		// activeChar.sendPacket(new ExJumpToLocation(activeChar));
	}
}