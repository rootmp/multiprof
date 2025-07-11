package l2s.gameserver.network.l2.c2s.adenadistribution;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.adenadistribution.ExDivideAdenaCancel;

/**
 * @author Erlandys
 */
public class RequestDivideAdenaCancel implements IClientIncomingPacket
{
	private int _cancel;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_cancel = packet.readC();
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

		if (_cancel == 0)
		{
			activeChar.sendPacket(SystemMsg.ADENA_DISTRIBUTION_HAS_BEEN_CANCELLED);
			activeChar.sendPacket(ExDivideAdenaCancel.STATIC);
		}
	}
}