package l2s.gameserver.network.l2.c2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.LuckyGameHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExStartLuckyGame;
import l2s.gameserver.templates.luckygame.LuckyGameData;
import l2s.gameserver.templates.luckygame.LuckyGameType;

/**
 * @author Bonux
 **/
public final class RequestLuckyGameStartInfo implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestLuckyGameStartInfo.class);

	private int _typeId;

	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_typeId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		if(!Config.ALLOW_LUCKY_GAME_EVENT)
			return;

		if(_typeId < 0 || _typeId >= LuckyGameType.VALUES.length)
			return;

		final LuckyGameType type = LuckyGameType.VALUES[_typeId];
		final LuckyGameData gameData = LuckyGameHolder.getInstance().getData(type);
		if(gameData == null)
		{
			_log.warn("Cannot find data for lucky game TYPE[" + type + "]!");
			return;
		}

		player.sendPacket(new ExStartLuckyGame(player, gameData));
	}
}