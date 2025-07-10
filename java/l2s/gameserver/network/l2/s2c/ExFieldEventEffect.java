package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExFieldEventEffect implements IClientOutgoingPacket
{
	private final int _unk;

	public ExFieldEventEffect(int unk)
	{
		_unk = unk;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_unk);
	}
}