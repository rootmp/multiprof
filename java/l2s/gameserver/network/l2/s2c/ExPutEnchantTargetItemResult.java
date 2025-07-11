package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExPutEnchantTargetItemResult implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket FAIL = new ExPutEnchantTargetItemResult(0);
	public static final IClientOutgoingPacket SUCCESS = new ExPutEnchantTargetItemResult(1);

	private int _result;

	public ExPutEnchantTargetItemResult(int result)
	{
		_result = result;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_result);
		return true;
	}
}