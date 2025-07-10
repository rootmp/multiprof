package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExPledgeWaitingList;

/**
 * @author GodWorld
 * @reworked by Bonux
 **/
public class RequestPledgeWaitingList implements IClientIncomingPacket
{
	private int _clanId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_clanId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExPledgeWaitingList(_clanId));
	}
}