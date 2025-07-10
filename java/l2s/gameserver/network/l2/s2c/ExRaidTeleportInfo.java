package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class ExRaidTeleportInfo extends L2GameServerPacket
{
	Player _player;

	public ExRaidTeleportInfo(Player player)
	{
		_player = player;
	}

	@Override
	public void writeImpl()
	{
		writeD(_player.getVarBoolean("freeRaidTeleport", true) ? 0 : 1);
	}
}
