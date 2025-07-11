package l2s.gameserver.network.l2.s2c.magiclamp;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExMagicLampGameResult implements IClientOutgoingPacket
{
	private int _gamesCount, _gameType;
	private Player _player;

	public ExMagicLampGameResult(int gamesCount, int gameType, Player player)
	{
		_gamesCount = gamesCount;
		_gameType = gameType;
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (_gameType == 0)
		{
			_player.setMagicLampPoints(_player.getMagicLampPoints() - (10000000 * _gamesCount));
			giveRewards(packetWriter , _player, false);
		}
		else
		{
			_player.setMagicLampPoints(_player.getMagicLampPoints() - (100000000 * _gamesCount));
			if (!_player.getInventory().destroyItemByItemId(91641, _gamesCount * 5))
				return false;
			giveRewards(packetWriter, _player, true);
		}

		_player.sendPacket(new ExMagicLampGameInfo(_gameType, _player, _gamesCount));
		_player.sendPacket(new ExMagicLampExpInfo(_player));
		return true;
	}

	private void giveRewards(PacketWriter packetWriter, Player player, boolean isSpecialGame)
	{
		int slot1wins = 0;
		int slot2wins = 0;
		int slot3wins = 0;
		int slot4wins = 0;
		for (int i = 0; i < _gamesCount; i++)
		{
			int number = Rnd.get(100);
			if (number < 10)
				slot1wins++;
			else if (number < 30)
				slot2wins++;
			else if (number < 60)
				slot3wins++;
			else
				slot4wins++;
		}

		packetWriter.writeD(_gamesCount); // games count

		long exp = 0;
		long sp = 0;
		int x = 1;
		if (isSpecialGame)
			x = 12;

		if (slot1wins > 0)
		{
			packetWriter.writeC(1); // slot id
			packetWriter.writeD(slot1wins); // count wins
			packetWriter.writeQ(100_000_000 * x); // exp reward per 1 win
			packetWriter.writeQ(2_700_000 * x); // sp reward per 1 win

			exp += (100_000_000 * x * slot1wins);
			sp += (2_700_000 * x * slot1wins);
		}
		if (slot2wins > 0)
		{
			packetWriter.writeC(2);
			packetWriter.writeD(slot2wins);
			packetWriter.writeQ(30_000_000 * x);
			packetWriter.writeQ(810_000 * x);

			exp += (30_000_000 * x * slot2wins);
			sp += (810_000 * x * slot2wins);
		}
		if (slot3wins > 0)
		{
			packetWriter.writeC(3);
			packetWriter.writeD(slot3wins);
			packetWriter.writeQ(10_000_000 * x);
			packetWriter.writeQ(270_000 * x);

			exp += (10_000_000 * x * slot3wins);
			sp += (270_000 * x * slot3wins);
		}
		if (slot4wins > 0)
		{
			packetWriter.writeC(4);
			packetWriter.writeD(slot4wins);
			packetWriter.writeQ(5_000_000 * x);
			packetWriter.writeQ(135_000 * x);

			exp += (5_000_000 * x * slot4wins);
			sp += (135_000 * x * slot4wins);
		}

		_player.addExpAndSp(exp, sp);
	}
}