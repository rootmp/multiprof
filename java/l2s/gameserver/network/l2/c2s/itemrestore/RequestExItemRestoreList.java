package l2s.gameserver.network.l2.c2s.itemrestore;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;

public class RequestExItemRestoreList extends L2GameClientPacket
{
	private int cCategory;

	@Override
	protected boolean readImpl()
	{
		cCategory = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.getEnchantBrokenItemList().showRestoreWindowCategory(cCategory);
	}
}