package l2s.gameserver.network.l2.c2s.magiclamp;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.magiclamp.ExMagicLampGameResult;

/**
 * @author nexvill
 */
public class RequestExMagicLampGameStart implements IClientIncomingPacket
{
	private int _gamesCount;
	private int _gameType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_gamesCount = packet.readD();
		_gameType = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (_gameType == 0)
		{
			if ((activeChar.getMagicLampPoints() / 10000000) < _gamesCount)
				return;
		}
		else
		{
			if (((activeChar.getMagicLampPoints() / 100000000) < _gamesCount) || ((activeChar.getInventory().getItemByItemId(91641).getCount() / 5) < _gamesCount))
				return;
		}

		activeChar.sendPacket(new ExMagicLampGameResult(_gamesCount, _gameType, activeChar));
	}
}