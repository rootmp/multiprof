package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;

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
	public void writeImpl()
	{
		packetWriter.writeD(_player.getVarBoolean("freeRaidTeleport", true) ? 0 : 1);
	}
}
