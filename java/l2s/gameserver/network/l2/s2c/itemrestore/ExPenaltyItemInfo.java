package l2s.gameserver.network.l2.s2c.itemrestore;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPenaltyItemInfo implements IClientOutgoingPacket
{
	private final Player _player;

	public ExPenaltyItemInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_player.getItemsToRestore().size()); // items to restore
		packetWriter.writeD(50); // items max count
	}
}