package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.base.SoulShotType;

public class ExAutoSoulShot implements IClientOutgoingPacket
{
	private final int _itemId;
	private final int _slotId;
	private final int _type;

	public ExAutoSoulShot(int itemId, int slotId, SoulShotType type)
	{
		_itemId = itemId;
		_slotId = slotId;
		_type = type.ordinal();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemId);
		packetWriter.writeD(_slotId);
		packetWriter.writeD(_type);
		return true;
	}
}