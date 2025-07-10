package l2s.gameserver.network.l2.c2s.teleport;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.teleport.ExRaidTeleportInfo;

/**
 * @author nexvill
 */
public class RequestExRaidTeleportInfo extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		readC(); // unused
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExRaidTeleportInfo(activeChar));
	}
}