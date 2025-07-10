package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExBlockDefailInfo implements IClientOutgoingPacket
{
	private final String _blockName;
	private final String _blockMemo;

	public ExBlockDefailInfo(String name, String memo)
	{
		_blockName = name;
		_blockMemo = memo;

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_blockName);
		packetWriter.writeS(_blockMemo);
	}
}
