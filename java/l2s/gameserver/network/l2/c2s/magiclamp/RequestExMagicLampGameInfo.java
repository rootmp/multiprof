package l2s.gameserver.network.l2.c2s.magiclamp;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.magiclamp.ExMagicLampGameInfo;

/**
 * @author nexvill
 */
public class RequestExMagicLampGameInfo implements IClientIncomingPacket
{
	private int _gameType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_gameType = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		activeChar.sendPacket(new ExMagicLampGameInfo(_gameType, activeChar, 1));
	}
}