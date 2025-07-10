package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class ExAdenaInvenCount implements IClientOutgoingPacket
{
	private final long _adena;
	private final int _useInventorySlots;

	public ExAdenaInvenCount(Player player)
	{
		_adena = player.getAdena();
		_useInventorySlots = player.getInventory().getSize();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_adena);
		packetWriter.writeH(_useInventorySlots);
		return true;
	}
}