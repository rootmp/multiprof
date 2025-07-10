package l2s.gameserver.network.l2.c2s.steadybox;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.steadybox.ExSteadyOneBoxUpdate;
import l2s.gameserver.templates.item.ItemTemplate;

/**
 * @author nexvill
 */
public class RequestExSteadyOpenBox extends L2GameClientPacket
{
	private int _slotId, _l2CoinCount;

	@Override
	protected boolean readImpl()
	{
		_slotId = readD();
		_l2CoinCount = readD();
		readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (_l2CoinCount > 0)
		{
			if (activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, _l2CoinCount))
			{
				activeChar.sendPacket(new ExSteadyOneBoxUpdate(activeChar, _slotId, false, true));
			}
		}
		else
			activeChar.sendPacket(new ExSteadyOneBoxUpdate(activeChar, _slotId, false, false));
	}
}
