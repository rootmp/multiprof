package l2s.gameserver.network.l2.c2s.magiclamp;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.magiclamp.ExMagicLampGameInfo;

/**
 * @author nexvill
 */
public class RequestExMagicLampGameInfo extends L2GameClientPacket
{
	private int _gameType;

	@Override
	protected boolean readImpl()
	{
		_gameType = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExMagicLampGameInfo(_gameType, activeChar, 1));
	}
}