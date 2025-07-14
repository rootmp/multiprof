package l2s.gameserver.network.l2.s2c.blessing;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExOpenBlessOptionScroll implements IClientOutgoingPacket
{
	private ItemInstance _scroll;
	private Player _player;

	public ExOpenBlessOptionScroll(Player player, ItemInstance scroll)
	{
		_player = player;
		_scroll = scroll;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		_player.setBlessingScroll(_scroll);
		packetWriter.writeD(_scroll.getItemId()); // scroll id
		return true;
	}
}