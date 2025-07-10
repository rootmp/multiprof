package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public final class ExPeriodicItemList implements IClientOutgoingPacket
{
	private final int _result;
	private final int _objectID;
	private final int _period;

	public ExPeriodicItemList(int result, int objectID, int period)
	{
		_result = result;
		_objectID = objectID;
		_period = period;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_result);
		packetWriter.writeD(_objectID);
		packetWriter.writeD(_period);
	}
}