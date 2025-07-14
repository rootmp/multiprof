package l2s.gameserver.network.l2.s2c.subjugation;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExSubjugationGachaUI implements IClientOutgoingPacket
{
	private int _zoneId;
	private Player _player;

	public ExSubjugationGachaUI(Player player, int zoneId)
	{
		_player = player;
		_zoneId = zoneId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int points = _player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + _zoneId, 0);
		int keysHave = points / 1_000_000;

		packetWriter.writeD(keysHave); // keys
		return true;
	}
}