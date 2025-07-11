package l2s.gameserver.network.l2.c2s.randomcraft;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftRandomMake;

/**
 * @author nexvill
 */
public class RequestExCraftRandomMake implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		if (activeChar.getCraftPoints() < 1)
			return;
		if (!activeChar.getInventory().reduceAdena(Config.RANDOM_CRAFT_TRY_COMMISSION))
		{
			return;
		}
		activeChar.sendPacket(new ExCraftRandomMake(activeChar));
	}
}