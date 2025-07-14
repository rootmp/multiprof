package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ShowTownMapPacket implements IClientOutgoingPacket
{
	/**
	 * Format: csdd
	 */

	String _texture;
	int _x;
	int _y;

	public ShowTownMapPacket(String texture, int x, int y)
	{
		_texture = texture;
		_x = x;
		_y = y;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_texture);
		packetWriter.writeD(_x);
		packetWriter.writeD(_y);
		return true;
	}
}