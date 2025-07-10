package l2s.gameserver.network.l2.c2s.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionList;

/**
 * @author nexvill
 */
public class RequestExCollectionList extends L2GameClientPacket
{
	private int _tabId;

	@Override
	protected boolean readImpl()
	{
		_tabId = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new ExCollectionList(player, _tabId));
	}
}