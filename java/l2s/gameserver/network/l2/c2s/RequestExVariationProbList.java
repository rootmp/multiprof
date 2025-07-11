package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExVariationProbList;

public class RequestExVariationProbList implements IClientIncomingPacket
{
	private int nRefineryID;
	private int nTargetItemId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nRefineryID = packet.readD();
		nTargetItemId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		player.sendPacket(new ExVariationProbList(player, nRefineryID, nTargetItemId));
	}

}
