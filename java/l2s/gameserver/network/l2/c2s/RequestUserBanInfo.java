package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExUserBanInfo;

/**
 * @author Bonux
 **/
public class RequestUserBanInfo implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		client.sendPacket(new ExUserBanInfo(0));
	}
}