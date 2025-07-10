package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExDominionChannelSet implements IClientOutgoingPacket
{
	public static final L2GameServerPacket ACTIVE = new ExDominionChannelSet(1);
	public static final L2GameServerPacket DEACTIVE = new ExDominionChannelSet(0);

	private int _active;

	public ExDominionChannelSet(int active)
	{
		_active = active;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_active);
	}
}