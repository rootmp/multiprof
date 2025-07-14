package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.PledgeReceiveWarList;

public class RequestPledgeWarList implements IClientIncomingPacket
{
	// format: (ch)dd
	static int _type;
	private int _page;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_page = packet.readD();
		_type = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		Clan clan = activeChar.getClan();
		if(clan == null)
			return;

		activeChar.sendPacket(new PledgeReceiveWarList(clan, _page));
	}
}