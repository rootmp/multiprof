package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author monithly
 */
public class ExLightingCandleEvent implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket ENABLED = new ExLightingCandleEvent(1);
	public static final IClientOutgoingPacket DISABLED = new ExLightingCandleEvent(0);

	private final int _value;

	public ExLightingCandleEvent(int value)
	{
		_value = value;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_value); // Available
		return true;
	}
}
