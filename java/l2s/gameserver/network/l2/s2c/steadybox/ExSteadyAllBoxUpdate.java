package l2s.gameserver.network.l2.s2c.steadybox;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExSteadyAllBoxUpdate extends L2GameServerPacket
{
	private Player _player;

	public ExSteadyAllBoxUpdate(Player player)
	{
		_player = player;
	}

	@Override
	protected final void writeImpl()
	{
		int count = _player.getVarInt(PlayerVariables.SB_BOX_SLOTS, 1);

		writeD(_player.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0)); // mobs killed
		writeD(_player.getVarInt(PlayerVariables.SB_KILLED_PLAYERS, 0)); // players killed
		writeD(count);

		for (int i = 1; i <= count; i++)
		{
			writeD(i); // slot id

			int boxType = _player.getVarInt(PlayerVariables.SB_BOX_TYPE + "_" + i, 1);
			long rewardTime = _player.getVarLong(PlayerVariables.SB_REWARD_TIME + "_" + i, -2);

			if (rewardTime == -2)
			{
				writeD(1); // open type - 1 not available, 2- can start timer, 3 - timer going, 4 - can get
							// reward
				writeD(0); // box type, 1 - basic, 2 - advanced, 3 - top
			}
			else if (rewardTime == -1)
			{
				writeD(2);
				writeD(boxType);
			}
			else
			{
				int diffTime = (int) ((rewardTime - System.currentTimeMillis()) / 1000);
				if (diffTime > 0)
				{
					writeD(3);
					writeD(boxType);
				}
				else
				{
					writeD(4);
					writeD(boxType);
				}
			}
		}

		int timeToReward = (int) ((_player.getVarLong(PlayerVariables.SB_REWARD_TIME + "_" + 1, -2) - System.currentTimeMillis()) / 1000);
		if (timeToReward < 0)
		{
			timeToReward = 0;
		}
		writeD(timeToReward); // timer in seconds to expire and box can open
	}
}