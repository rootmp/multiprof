package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;

public class ExMagicSkillUseGround extends L2GameServerPacket
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
	protected final void writeImpl()
	{
		writeD(_player.getObjectId());
		writeD(47001);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
	}
}