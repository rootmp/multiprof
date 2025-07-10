package l2s.gameserver.network.l2.c2s.blessing;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.blessing.ExBlessOptionCancel;

/**
 * @author Hl4p3x
 */
public class RequestExBlessOptionCancel implements IClientIncomingPacket
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
		{
			return;
		}

		activeChar.setEnchantScroll(null);
		activeChar.sendPacket(ExBlessOptionCancel.STATIC);
	}
}