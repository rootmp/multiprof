package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ChooseInventoryItemPacket implements IClientOutgoingPacket
{
	private int ItemID;

	public ChooseInventoryItemPacket(int id)
	{
		ItemID = id;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(ItemID);
		return true;
	}
}