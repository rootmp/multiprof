package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestExTimerCheck implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int nType;
	@SuppressWarnings("unused")
	private int nIndex;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nType = packet.readD();
		nIndex = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
		{ return; }
		//System.out.println("nType: " + nType + " nIndex:" +  nIndex);
	}

}
