package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExEnchantFail;

/**
 * @author Bonux
 **/
public class RequestNewEnchantClose implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		activeChar.setSynthesisItem1(null);
		activeChar.setSynthesisItem2(null);
		activeChar.sendPacket(ExEnchantFail.STATIC); // TODO: Check this.
	}
}