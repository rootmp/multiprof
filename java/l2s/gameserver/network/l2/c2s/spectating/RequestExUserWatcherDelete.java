package l2s.gameserver.network.l2.c2s.spectating;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;

/**
 * @author nexvill
 */
public class RequestExUserWatcherDelete extends L2GameClientPacket
{
	String _name;

	@Override
	protected boolean readImpl()
	{
		_name = readString();
		readD(); // 0
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.getSpectatingList().remove(_name);
	}
}
