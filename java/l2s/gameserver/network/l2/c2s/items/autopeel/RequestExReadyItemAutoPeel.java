package l2s.gameserver.network.l2.c2s.items.autopeel;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.items.autopeel.ExReadyItemAutoPeel;

/**
 * @author nexvill
 */
public class RequestExReadyItemAutoPeel extends L2GameClientPacket
{
	private int _itemObjId;

	@Override
	protected boolean readImpl()
	{
		_itemObjId = readD();
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

		if (activeChar.getInventory().getItemByObjectId(_itemObjId).getCount() != 0)
		{
			activeChar.sendPacket(new ExReadyItemAutoPeel(1, _itemObjId));
		}
		else
		{
			activeChar.sendPacket(new ExReadyItemAutoPeel(0, _itemObjId));
		}
	}
}