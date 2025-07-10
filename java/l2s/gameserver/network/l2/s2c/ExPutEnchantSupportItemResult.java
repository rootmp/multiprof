package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExPutEnchantSupportItemResult implements IClientOutgoingPacket
{
	public static final L2GameServerPacket FAIL = new ExPutEnchantSupportItemResult(0x01);
	public static final L2GameServerPacket SUCCESS = new ExPutEnchantSupportItemResult(0x01);

	private int _result;

	public ExPutEnchantSupportItemResult(int result)
	{
		_result = result;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_result);
	}
}