package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExApplyVariationOption implements IClientOutgoingPacket
{
	private final int _result;
	private final int _enchantedObjectId;
	private final int _option1;
	private final int _option2;
	private final int _option3;

	public ExApplyVariationOption(int result, int enchantedObjectId, int option1, int option2, int option3)
	{
		_result = result;
		_enchantedObjectId = enchantedObjectId;
		_option1 = option1;
		_option2 = option2;
		_option3 = option3;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_result);
		packetWriter.writeD(_enchantedObjectId);
		packetWriter.writeD(_option1);
		packetWriter.writeD(_option2);
		packetWriter.writeD(_option3);
		return true;
	}

}
