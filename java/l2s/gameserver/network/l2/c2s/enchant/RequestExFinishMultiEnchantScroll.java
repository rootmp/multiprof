package l2s.gameserver.network.l2.c2s.enchant;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;

/**
 * @author nexvill
 */
public class RequestExFinishMultiEnchantScroll extends L2GameClientPacket
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
		final Player player = getClient().getActiveChar();
		if (player == null)
			return;

		player.setEnchantItem(null);
		player.setEnchantScroll(null);
		player.getMultiFailureEnchantList().clear();
		player.getMultiSuccessEnchantList().clear();
	}
}