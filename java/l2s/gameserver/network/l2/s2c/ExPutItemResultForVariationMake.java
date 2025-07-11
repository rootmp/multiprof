package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExPutItemResultForVariationMake implements IClientOutgoingPacket
{
	private int _itemObjId;
	private int _unk1;
	private int _unk2;

	public ExPutItemResultForVariationMake(int itemObjId)
	{
		_itemObjId = itemObjId;
		_unk1 = 1;
		_unk2 = 1;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemObjId);
		packetWriter.writeD(_unk1);
		packetWriter.writeD(_unk2);
		return true;
	}
}