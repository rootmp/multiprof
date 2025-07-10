package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExElementalSpiritGetExp implements IClientOutgoingPacket
{
	private final int _elementId;
	private final long _exp;

	public ExElementalSpiritGetExp(int elementId, long exp)
	{
		_elementId = elementId;
		_exp = exp;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_elementId);
		packetWriter.writeQ(_exp);
	}
}