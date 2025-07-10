package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * sample: d
 */
public class ShowCalcPacket implements IClientOutgoingPacket
{
	private int _calculatorId;

	public ShowCalcPacket(int calculatorId)
	{
		_calculatorId = calculatorId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_calculatorId);
	}
}