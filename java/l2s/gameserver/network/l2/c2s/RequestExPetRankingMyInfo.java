package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExPetRankingMyInfo;

public class RequestExPetRankingMyInfo implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int nCollarID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nCollarID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		player.sendPacket(new ExPetRankingMyInfo(player));
	}
}