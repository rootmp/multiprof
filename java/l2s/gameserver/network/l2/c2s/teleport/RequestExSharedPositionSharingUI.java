package l2s.gameserver.network.l2.c2s.teleport;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.teleport.ExSharedPositionSharingUI;

/**
 * @author nexvill
 */
public class RequestExSharedPositionSharingUI implements IClientIncomingPacket
{

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();

		player.sendPacket(new ExSharedPositionSharingUI(player));
	}
}