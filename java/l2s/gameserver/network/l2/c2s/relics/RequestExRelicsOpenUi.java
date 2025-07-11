package l2s.gameserver.network.l2.c2s.relics;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExRelicsOpenUi implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
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
		Player player = client.getActiveChar();
		if(player == null)
			return;
		player.getRelics().checkUpdate();
	}

}
