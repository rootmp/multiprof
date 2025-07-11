package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

public class ExBR_GamePointPacket implements IClientOutgoingPacket
{
	private int _objectId;
	private long _points;

	public ExBR_GamePointPacket(Player player)
	{
		_objectId = player.getObjectId();
		_points = player.getPremiumPoints();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);
		packetWriter.writeQ(_points);
		packetWriter.writeD(0x00); // ??
		return true;
	}
}