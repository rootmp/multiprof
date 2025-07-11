package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExChooseCostumeItem implements IClientOutgoingPacket
{
	private final int itemId;

	public ExChooseCostumeItem(int itemId)
	{
		this.itemId = itemId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(itemId); // ItemClassID*/
		return true;
	}
}