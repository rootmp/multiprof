package l2s.gameserver.network.l2.s2c.adenadistribution;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author Erlandys
 */
public class ExDivideAdenaDone implements IClientOutgoingPacket
{
	private final int _friendsCount;
	private final long _count, _dividedCount;
	private final String _name;

	public ExDivideAdenaDone(int friendsCount, long count, long dividedCount, String name)
	{
		_friendsCount = friendsCount;
		_count = count;
		_dividedCount = dividedCount;
		_name = name;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0x01); // Always 1
		packetWriter.writeC(0x00); // Always 0
		packetWriter.writeD(_friendsCount); // Friends count
		packetWriter.writeQ(_dividedCount); // Divided count
		packetWriter.writeQ(_count); // Whole count
		packetWriter.writeS(_name); // Giver name
		return true;
	}
}