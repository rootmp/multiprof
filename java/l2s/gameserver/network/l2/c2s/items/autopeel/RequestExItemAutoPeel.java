package l2s.gameserver.network.l2.c2s.items.autopeel;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.items.autopeel.ExResultItemAutoPeel;

/**
 * @author nexvill
 */
public class RequestExItemAutoPeel extends L2GameClientPacket
{
	private int _itemObjId;
	private long _totalCount;
	private long _remainCount;

	@Override
	protected boolean readImpl()
	{
		_itemObjId = readD();
		_totalCount = readQ();
		_remainCount = readQ();
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

		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isInStoreMode())
		{
			activeChar.sendActionFailed();
			return;
		}

		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(new ExResultItemAutoPeel(activeChar, item, _totalCount, _remainCount));
	}
}