package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

/**
 * @author Bonux
 **/
public final class RequestExEscapeScene implements IClientIncomingPacket
{
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(!activeChar.isInMovie())
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.endScenePlayer(true);
	}
}