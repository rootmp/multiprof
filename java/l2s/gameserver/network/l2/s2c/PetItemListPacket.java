package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.model.items.ItemInstance;

public class PetItemListPacket implements IClientOutgoingPacket
{
	private final ItemInstance[] items;

	public PetItemListPacket(PetInstance cha)
	{
		items = cha.getInventory().getItems();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(items.length);

		for (ItemInstance item : items)
			writeItemInfo(packetWriter, item);
		
		return true;
	}
}