package l2s.gameserver.network.l2.c2s.subjugation;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationList;

/**
 * @author nexvill
 */
public class RequestExSubjugationList extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new ExSubjugationList(player));
	}
}