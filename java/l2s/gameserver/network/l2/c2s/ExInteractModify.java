package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

public class ExInteractModify implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		/*packet.readC();
		packet.readH();
		packet.readC();
		packet.readC();*/
		return true;
	}

	@Override
	public void run(GameClient client)
	{

	}
}