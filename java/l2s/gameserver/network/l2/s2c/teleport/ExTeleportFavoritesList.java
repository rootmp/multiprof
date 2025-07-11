package l2s.gameserver.network.l2.s2c.teleport;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExTeleportFavoritesList implements IClientOutgoingPacket
{
	private final boolean _activate;
	private int[] _teleportFavorites;

	public ExTeleportFavoritesList(boolean activate, Player player)
	{
		_activate = activate;
		_teleportFavorites = player.getTeleportFavorites().toArray();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_activate);
		packetWriter.writeD(_teleportFavorites.length); // teleports size
		for (int i = 0; i < _teleportFavorites.length; i++)
		{
			packetWriter.writeD(_teleportFavorites[i]); // teleport id
		}
		return true;
	}
}