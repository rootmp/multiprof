package l2s.gameserver.network.l2.c2s.randomcraft;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftRandomLockSlot;

/**
 * @author nexvill
 */
public class RequestExCraftRandomLockSlot implements IClientIncomingPacket
{
	int _slot;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slot = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExCraftRandomLockSlot(activeChar, _slot));
	}
}