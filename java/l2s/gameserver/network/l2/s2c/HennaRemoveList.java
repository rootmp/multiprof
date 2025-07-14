package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

/**
 * @author Zoey76
 */
public class HennaRemoveList implements IClientOutgoingPacket
{
	@SuppressWarnings("unused")
	private final Player _player;

	public HennaRemoveList(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//OutgoingPackets.HENNA_UNEQUIP_LIST.writeId(packet);
		// packetWriter.writeQ(_player.getAdena());
		// packetWriter.writeD(3); // seems to be max size
		// packetWriter.writeD(3 - _player.getHennaEmptySlots());
		// for (Henna henna : _player.getHennaList())
		// {
		// if (henna != null)
		// {
		// packetWriter.writeD(henna.getDyeId());
		// packetWriter.writeD(henna.getDyeItemId());
		// packetWriter.writeQ(henna.getCancelCount());
		// packetWriter.writeQ(henna.getCancelFee());
		// packetWriter.writeD(0);
		// }
		// }
		return true;
	}
}
