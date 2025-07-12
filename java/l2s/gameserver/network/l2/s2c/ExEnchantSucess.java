package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
**/
public final class ExEnchantSucess implements IClientOutgoingPacket
{
	private final int _itemId;
	private int _enchant;

	public ExEnchantSucess(int itemId)
	{
		_itemId = itemId;
		_enchant = 0;
	}

	public ExEnchantSucess(int id, int enchant)
	{
		_itemId = id;
		_enchant = enchant;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemId);
		packetWriter.writeD(_enchant);
		return true;
	}
}