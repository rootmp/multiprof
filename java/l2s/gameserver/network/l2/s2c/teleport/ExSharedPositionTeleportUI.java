package l2s.gameserver.network.l2.s2c.teleport;

import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExSharedPositionTeleportUI implements IClientOutgoingPacket
{
	private Player _player;
	private int _tpId;

	public ExSharedPositionTeleportUI(Player player, int tpId)
	{
		_player = player;
		_tpId = tpId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (ServerVariables.getString("tpId_" + _tpId + "_name") != null)
		{
			packetWriter.writeString(ServerVariables.getString("tpId_" + _tpId + "_name"));
			packetWriter.writeD(_tpId);
			packetWriter.writeD(_player.getVarInt(PlayerVariables.SHARED_POSITION_TELEPORTS, Config.SHARED_TELEPORTS_PER_DAY)); // exist
																													// teleports
			packetWriter.writeH(150); // ?? also not work with low values like 0 or 1
			packetWriter.writeD(ServerVariables.getInt("tpId_" + _tpId + "_x"));
			packetWriter.writeD(ServerVariables.getInt("tpId_" + _tpId + "_y"));
			packetWriter.writeD(ServerVariables.getInt("tpId_" + _tpId + "_z"));
		}
		
		return true;
	}
}