package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;

public class RequestFirstPlayStart extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;
	}
}