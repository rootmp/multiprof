package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExPutEnchantScrollItemResult implements IClientOutgoingPacket
{
	public static final L2GameServerPacket FAIL = new ExPutEnchantScrollItemResult(0x00);

	private int _result;

	public ExPutEnchantScrollItemResult(int result)
	{
		_result = result;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_result);
	}
}