package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.GameObject;

/**
 * format dddddd (player id, target id, distance, startx, starty, startz)
 * <p>
 */
public class ValidateLocationPacket implements IClientOutgoingPacket
{
	private int _chaObjId;
	private Location _loc;

	public ValidateLocationPacket(GameObject cha)
	{
		_chaObjId = cha.getObjectId();
		_loc = cha.getLoc();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_chaObjId);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		packetWriter.writeD(_loc.h);
		packetWriter.writeC(0xFF);
		return true;
	}
}