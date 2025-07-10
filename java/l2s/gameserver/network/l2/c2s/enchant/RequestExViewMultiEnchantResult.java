package l2s.gameserver.network.l2.c2s.enchant;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.enchant.ExResultMultiEnchantItemList;

/**
 * @author nexvill
 */
public class RequestExViewMultiEnchantResult extends L2GameClientPacket
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

		player.sendPacket(new ExResultMultiEnchantItemList(player, player.getMultiSuccessEnchantList(), player.getMultiFailureEnchantList(), true));
	}
}