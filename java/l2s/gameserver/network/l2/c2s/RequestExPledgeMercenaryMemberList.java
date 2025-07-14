package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExPledgeMercenaryMemberList;

public class RequestExPledgeMercenaryMemberList implements IClientIncomingPacket
{
	private int castleId;
	private int clanId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		castleId = packet.readD();
		clanId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		activeChar.sendPacket(new ExPledgeMercenaryMemberList(activeChar, castleId, clanId));
	}
}
