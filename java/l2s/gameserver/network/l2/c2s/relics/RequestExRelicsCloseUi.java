package l2s.gameserver.network.l2.c2s.relics;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExRelicsCloseUi implements IClientIncomingPacket
{
	private short cDummy;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cDummy = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

	}

}
