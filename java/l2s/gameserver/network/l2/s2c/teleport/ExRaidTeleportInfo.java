package l2s.gameserver.network.l2.s2c.teleport;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExRaidTeleportInfo implements IClientOutgoingPacket
{
	Player _player;

	public ExRaidTeleportInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int usedFreeTeleports = _player.getVarInt(PlayerVariables.FREE_RAID_TELEPORTS_USED, 0);
		if (usedFreeTeleports == 0)
			packetWriter.writeD(0);
		else
			packetWriter.writeD(1);
		
		return true;
	}
}
