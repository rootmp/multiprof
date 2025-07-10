package l2s.gameserver.network.l2.c2s.items.autopeel;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.items.autopeel.ExStopItemAutoPeel;

/**
 * @author nexvill
 */
public class RequestExStopItemAutoPeel implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readC(); // cDummy
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		activeChar.sendPacket(new ExStopItemAutoPeel(1));
		activeChar.sendItemList(false);
	}
}