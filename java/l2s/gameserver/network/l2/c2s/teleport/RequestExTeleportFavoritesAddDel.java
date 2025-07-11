package l2s.gameserver.network.l2.c2s.teleport;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;

/**
 * @author nexvill
 */
public class RequestExTeleportFavoritesAddDel implements IClientIncomingPacket
{
	private boolean _type;
	private int _teleportId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_type = packet.readC() == 1 ? true : false;
		_teleportId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (_type)
		{
			activeChar.addTeleportFavorite(_teleportId);
		}
		else
		{
			activeChar.removeTeleportFavorite(_teleportId);
		}
	}
}