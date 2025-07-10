package l2s.gameserver.network.l2.c2s.subjugation;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationGachaUI;

/**
 * @author nexvill
 */
public class RequestExSubjugationGachaUI extends L2GameClientPacket
{
	private int _zoneId;

	@Override
	protected boolean readImpl()
	{
		_zoneId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new ExSubjugationGachaUI(player, _zoneId));
	}
}