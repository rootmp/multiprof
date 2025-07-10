package l2s.gameserver.network.l2.s2c.itemrestore;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPenaltyItemInfo extends L2GameServerPacket
{
	private final Player _player;

	public ExPenaltyItemInfo(Player player)
	{
		_player = player;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_player.getItemsToRestore().size()); // items to restore
		writeD(50); // items max count
	}
}