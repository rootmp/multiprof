package l2s.gameserver.network.l2.c2s.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionOpenUI;

/**
 * @author nexvill
 */
public class RequestExCollectionOpenUI extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		readC(); // not used
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
		if (player == null)
			return;

		player.setInCollectionUI(true);
		player.sendPacket(new ExCollectionOpenUI());
	}
}