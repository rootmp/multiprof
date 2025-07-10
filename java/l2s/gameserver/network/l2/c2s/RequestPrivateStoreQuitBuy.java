package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;

public class RequestPrivateStoreQuitBuy extends L2GameClientPacket
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

		if (!activeChar.isInStoreMode() || activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_BUY)
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
		activeChar.storePrivateStore();
		activeChar.standUp();
		activeChar.broadcastCharInfo();
	}
}