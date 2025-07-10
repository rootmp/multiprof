package l2s.gameserver.network.l2.c2s.teleport;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.teleport.ExTeleportFavoritesList;

/**
 * @author nexvill
 */
public class RequestExTeleportFavoritesUIToggle implements IClientIncomingPacket
{
	private boolean _on;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_on = readC() == 1 ? true : false;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExTeleportFavoritesList(_on, activeChar));
	}
}