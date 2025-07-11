package l2s.gameserver.network.l2.s2c.steadybox;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExSteadyAllBoxUpdate implements IClientOutgoingPacket
{
	private Player _player;

	public ExSteadyAllBoxUpdate(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int count = _player.getVarInt(PlayerVariables.SB_BOX_SLOTS, 1);

		packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0)); // mobs killed
		packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_PLAYERS, 0)); // players killed
		packetWriter.writeD(count);

		for (int i = 1; i <= count; i++)
		{
			packetWriter.writeD(i); // slot id

			int boxType = _player.getVarInt(PlayerVariables.SB_BOX_TYPE + "_" + i, 1);
			long rewardTime = _player.getVarLong(PlayerVariables.SB_REWARD_TIME + "_" + i, -2);

			if (rewardTime == -2)
			{
				packetWriter.writeD(1); // open type - 1 not available, 2- can start timer, 3 - timer going, 4 - can get
							// reward
				packetWriter.writeD(0); // box type, 1 - basic, 2 - advanced, 3 - top
			}
			else if (rewardTime == -1)
			{
				packetWriter.writeD(2);
				packetWriter.writeD(boxType);
			}
			else
			{
				int diffTime = (int) ((rewardTime - System.currentTimeMillis()) / 1000);
				if (diffTime > 0)
				{
					packetWriter.writeD(3);
					packetWriter.writeD(boxType);
				}
				else
				{
					packetWriter.writeD(4);
					packetWriter.writeD(boxType);
				}
			}
		}

		int timeToReward = (int) ((_player.getVarLong(PlayerVariables.SB_REWARD_TIME + "_" + 1, -2) - System.currentTimeMillis()) / 1000);
		if (timeToReward < 0)
		{
			timeToReward = 0;
		}
		packetWriter.writeD(timeToReward); // timer in seconds to expire and box can open
		return true;
	}
}