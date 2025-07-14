package l2s.gameserver.network.l2.c2s.spExtract;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.spExtract.ExSpExtractInfo;

public class RequestExSpExtractInfo implements IClientIncomingPacket
{
	private int _nItemID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_nItemID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		player.sendPacket(new ExSpExtractInfo(player, _nItemID));
	}
}