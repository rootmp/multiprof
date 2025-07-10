package l2s.gameserver.network.l2.c2s.steadybox;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.steadybox.ExSteadyAllBoxUpdate;

/**
 * @author nexvill
 */
public class RequestExSteadyBoxLoad implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExSteadyAllBoxUpdate(activeChar));
	}
}
