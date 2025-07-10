package l2s.gameserver.network.l2.c2s.steadybox;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.steadybox.ExSteadyAllBoxUpdate;

/**
 * @author nexvill
 */
public class RequestExSteadyBoxLoad extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExSteadyAllBoxUpdate(activeChar));
	}
}
