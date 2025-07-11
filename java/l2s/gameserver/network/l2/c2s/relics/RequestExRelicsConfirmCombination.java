package l2s.gameserver.network.l2.c2s.relics;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExRelicsConfirmCombination implements IClientIncomingPacket
{
	private int nGrade;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nGrade = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

	}
}
