package l2s.gameserver.network.l2.s2c.teleport;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

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
		int usedFreeTeleports = _player.getVarInt(PlayerVariables.FREE_RAID_TELEPORTS_USED, 0);
		if (usedFreeTeleports == 0)
			writeD(0);
		else
			writeD(1);
	}
}
