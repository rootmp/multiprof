package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExPutIntensiveResultForVariationMake implements IClientOutgoingPacket
{
	private int _refinerItemObjId, _lifestoneItemId, _gemstoneItemId, _unk;
	private long _gemstoneCount;

	public ExPutIntensiveResultForVariationMake(int refinerItemObjId, int lifeStoneId, int gemstoneItemId, long gemstoneCount)
	{
		_refinerItemObjId = refinerItemObjId;
		_lifestoneItemId = lifeStoneId;
		_gemstoneItemId = gemstoneItemId;
		_gemstoneCount = gemstoneCount;
		_unk = 1;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_refinerItemObjId);
		packetWriter.writeD(_lifestoneItemId);
		packetWriter.writeD(_gemstoneItemId);
		packetWriter.writeQ(_gemstoneCount);
		packetWriter.writeD(_unk);
		return true;
	}
}