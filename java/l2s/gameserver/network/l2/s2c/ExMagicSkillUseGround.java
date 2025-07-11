package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;

public class ExMagicSkillUseGround implements IClientOutgoingPacket
{
	private Player _player;
	private Location _loc;

	/**
	 * @param player, location
	 */
	public ExMagicSkillUseGround(Player player, Location loc)
	{
		_player = player;
		_loc = loc;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_player.getObjectId());
		packetWriter.writeD(47001);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		return true;
	}
}