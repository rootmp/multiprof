package l2s.gameserver.network.l2.c2s.teleport;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.teleport.ExTeleportFavoritesList;

/**
 * @author nexvill
 */
public class RequestExTeleportFavoritesList implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExTeleportFavoritesList(true, activeChar));
	}
}