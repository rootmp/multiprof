package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExLoadPetPreview;

public class RequestExLoadPetPreviewByDbid implements IClientIncomingPacket
{
	private int nPetCollarDBId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nPetCollarDBId = packet.readD();
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
