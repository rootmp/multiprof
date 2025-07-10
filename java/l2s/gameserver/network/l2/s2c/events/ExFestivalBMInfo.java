package l2s.gameserver.network.l2.s2c.events;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExFestivalBMInfo extends L2GameServerPacket
{
	private final Player _player;

	public ExFestivalBMInfo(Player player)
	{
		_player = player;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(Config.BM_FESTIVAL_ITEM_TO_PLAY);

		if (Config.BM_FESTIVAL_PLAY_LIMIT != -1)
		{
			writeQ(_player.getVarInt("FESTIVAL_BM_EXIST_GAMES", Config.BM_FESTIVAL_PLAY_LIMIT));
		}
		else
		{
			writeQ(0);
		}

		writeD(Config.BM_FESTIVAL_ITEM_TO_PLAY_COUNT);
	}
}