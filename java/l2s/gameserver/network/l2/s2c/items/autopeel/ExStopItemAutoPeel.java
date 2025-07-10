package l2s.gameserver.network.l2.s2c.items.autopeel;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExStopItemAutoPeel implements IClientOutgoingPacket
{
	private static int _result;

	public ExStopItemAutoPeel(int result)
	{
		_result = result;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_result); // result
	}
}