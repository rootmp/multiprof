package l2s.gameserver.network.l2.s2c.steadybox;

import l2s.commons.network.PacketWriter;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExSteadyOneBoxUpdate implements IClientOutgoingPacket
{
	private Player _player;
	private int _slotId;
	private boolean _reward, _reset;

	public ExSteadyOneBoxUpdate(Player player, int slotId, boolean reward, boolean reset)
	{
		_player = player;
		_slotId = slotId;
		_reward = reward;
		_reset = reset;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if(_reward)
		{
			if((_player.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0) == Config.STEADY_BOX_KILL_MOBS)
					|| (_player.getVarInt(PlayerVariables.SB_KILLED_PLAYERS, 0) == Config.STEADY_BOX_KILL_PLAYERS))
			{
				_player.setVar(PlayerVariables.SB_REWARD_TIME + "_" + _slotId, -1);
				int boxType = 0;
				int chance = Rnd.get(100);
				if(chance > 30)
					boxType = 1;
				else if(chance > 5)
					boxType = 2;
				else
					boxType = 3;
				_player.setVar(PlayerVariables.SB_BOX_TYPE + "_" + _slotId, boxType);
				packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0));
				packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_PLAYERS, 0));
				packetWriter.writeH(14);
				packetWriter.writeD(_slotId);
				packetWriter.writeD(2); // open type - 1 not available, 2- can start timer, 3 - timer going, 4 - can get
				// reward
				packetWriter.writeD(boxType); // box type, 1 - basic, 2 - advanced, 3 - top
				packetWriter.writeD(0); // timer in seconds to can get reward
			}
			else
			{
				_player.setVar(PlayerVariables.SB_REWARD_TIME + "_" + _slotId, -2);
				packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0));
				packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_PLAYERS, 0));
				packetWriter.writeH(14);
				packetWriter.writeD(_slotId);
				packetWriter.writeD(1);
				packetWriter.writeD(0);
				packetWriter.writeD(0);
			}
		}
		else if(_reset)
		{
			int boxType = _player.getVarInt(PlayerVariables.SB_BOX_TYPE + "_" + _slotId, 1);
			_player.setVar(PlayerVariables.SB_REWARD_TIME + "_" + _slotId, 0);

			packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0));
			packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_PLAYERS, 0));
			packetWriter.writeH(14);
			packetWriter.writeD(_slotId);
			packetWriter.writeD(4);
			packetWriter.writeD(boxType);
			packetWriter.writeD(0);
		}
		else
		{
			if(_player.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0) == Config.STEADY_BOX_KILL_MOBS)
				_player.setVar(PlayerVariables.SB_KILLED_MOBS, 0);
			else
				_player.setVar(PlayerVariables.SB_KILLED_PLAYERS, 0);

			int boxType = _player.getVarInt(PlayerVariables.SB_BOX_TYPE + "_" + _slotId, 1);
			int timer = 0;
			if(boxType == 1)
				timer = 7200;
			else if(boxType == 2)
				timer = 21600;
			else
				timer = 43200;

			_player.setVar(PlayerVariables.SB_REWARD_TIME + "_" + _slotId, System.currentTimeMillis() + (timer * 1000));

			packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0));
			packetWriter.writeD(_player.getVarInt(PlayerVariables.SB_KILLED_PLAYERS, 0));
			packetWriter.writeH(14);
			packetWriter.writeD(_slotId);
			packetWriter.writeD(3);
			packetWriter.writeD(boxType);
			packetWriter.writeD(timer);
		}
		return true;
	}
}