package l2s.gameserver.network.l2.c2s.adenadistribution;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.adenadistribution.ExDivideAdenaCancel;

/**
 * @author Erlandys
 */
public class RequestDivideAdenaCancel extends L2GameClientPacket
{
	private int _cancel;

	@Override
	protected boolean readImpl()
	{
		_cancel = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		if (_cancel == 0)
		{
			activeChar.sendPacket(SystemMsg.ADENA_DISTRIBUTION_HAS_BEEN_CANCELLED);
			activeChar.sendPacket(ExDivideAdenaCancel.STATIC);
		}
	}
}