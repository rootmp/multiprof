package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExLoadPetPreview;

public class RequestExLoadPetPreviewBySid implements IClientIncomingPacket
{
	private int nPetCollarServerId;
	private short bIsInWorldServer;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nPetCollarServerId = packet.readD();
		bIsInWorldServer = packet.readC();

		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		player.sendPacket(new ExLoadPetPreview());
	}
}
