package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author SYS
 * @date 10/9/2007
 */
public class EventTriggerPacket implements IClientOutgoingPacket
{
	private final int _trapId;
	private final boolean _active;

	public EventTriggerPacket(int trapId, boolean active)
	{
		_trapId = trapId;
		_active = active;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_trapId); // trap object id
		packetWriter.writeC(_active ? 1 : 0); // trap activity 1 or 0
	}
}