package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExVariationResult implements IClientOutgoingPacket
{
	private int _variation1Id;
	private int _variation2Id;
	private int _variation3Id;
	private int _unk3;

	public ExVariationResult(int variation1Id, int variation2Id, int variation3Id, int unk3)
	{
		_variation1Id = variation1Id;
		_variation2Id = variation2Id;
		_variation3Id = variation3Id;
		_unk3 = unk3;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_variation1Id);
		packetWriter.writeD(_variation2Id);
		packetWriter.writeD(_variation3Id);
		
		packetWriter.writeD(_unk3);
		return true;
	}
}