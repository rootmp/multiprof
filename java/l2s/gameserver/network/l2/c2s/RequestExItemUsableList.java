package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

public class RequestExItemUsableList implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int cDummy;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cDummy = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		//Player player = client.getActiveChar();
		//System.out.println("" + cDummy);
	}
}