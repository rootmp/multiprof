package l2s.gameserver.network.l2.c2s.pvpbook;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpBookShareRevengeList;

/**
 * @author nexvill
 */
public class RequestExPvpBookShareRevengeList implements IClientIncomingPacket
{
	private int _userId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_userId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExPvpBookShareRevengeList(activeChar));
	}
}