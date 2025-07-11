package l2s.gameserver.network.l2.s2c.magiclamp;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExMagicLampGameInfo implements IClientOutgoingPacket
{
	private int _gameType;
	private Player _player;
	private int _gamesCount;
	private static final int SAYHAS_BLESSING = 91641;
	private int MAX_LAMP_EXP = Config.MAGIC_LAMP_MAX_LEVEL_EXP;

	public ExMagicLampGameInfo(int gameType, Player player, int gamesCount)
	{
		_gameType = gameType;
		_player = player;
		_gamesCount = gamesCount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int lampsExist = 0;
		if ((_player.getMagicLampPoints() / MAX_LAMP_EXP) >= 1)
		{
			lampsExist = (int) (_player.getMagicLampPoints() / MAX_LAMP_EXP);
		}

		packetWriter.writeD(300); // max games
		packetWriter.writeD(_gamesCount); // games count
		packetWriter.writeD(_gameType == 1 ? Config.MAGIC_LAMP_GREATER_CONSUME_COUNT : Config.MAGIC_LAMP_CONSUME_COUNT); // lamps consumed per game
		packetWriter.writeD(lampsExist); // existed lamps on player
		packetWriter.writeC(_gameType); // game type (standard or extended)
		packetWriter.writeD(_gameType == 1 ? 1 : 0); // if extended game, additional Sayha's Blessing used

		if (_gameType == 1) // if extended game selected
		{
			packetWriter.writeD(1); // items size for one game
			packetWriter.writeD(SAYHAS_BLESSING); // item id
			packetWriter.writeQ(5); // item count
			packetWriter.writeQ(_player.getInventory().getItemByItemId(SAYHAS_BLESSING).getCount()); // items count in player inventory
		}
		else
		{
			packetWriter.writeD(0); // items size for one game
		}
		return true;
	}
}