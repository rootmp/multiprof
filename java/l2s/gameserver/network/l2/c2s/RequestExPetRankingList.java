package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExPetRankingList;

public class RequestExPetRankingList implements IClientIncomingPacket
{
	private int cRankingGroup;
	private int cRankingScope;
	private int nIndex;
	private int nCollarID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cRankingGroup = packet.readC();
		cRankingScope = packet.readC();
		nIndex = packet.readH();
		nCollarID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		player.sendPacket(new ExPetRankingList(player, cRankingGroup, cRankingScope, nIndex, nCollarID));
	}
}