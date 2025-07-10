package l2s.gameserver.network.l2.c2s.itemrestore;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;

public class RequestExItemRestore extends L2GameClientPacket
{
	private int nBrokenItemClassID;
	private int cEnchant;

	@Override
	protected boolean readImpl() throws Exception
	{
		nBrokenItemClassID = readD();
		cEnchant = readC();
		return true;
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.getEnchantBrokenItemList().restoreItem(nBrokenItemClassID, cEnchant);
	}
}