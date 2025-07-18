package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Creature;

/**
 * Format (ch)dddcc
 */
public class ExFishingStartCombatPacket implements IClientOutgoingPacket
{
	int _time, _hp;
	int _lureType, _deceptiveMode, _mode;
	private int char_obj_id;

	public ExFishingStartCombatPacket(Creature character, int time, int hp, int mode, int lureType, int deceptiveMode)
	{
		char_obj_id = character.getObjectId();
		_time = time;
		_hp = hp;
		_mode = mode;
		_lureType = lureType;
		_deceptiveMode = deceptiveMode;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(char_obj_id);
		packetWriter.writeD(_time);
		packetWriter.writeD(_hp);
		packetWriter.writeC(_mode); // mode: 0 = resting, 1 = fighting
		packetWriter.writeC(_lureType); // 0 = newbie lure, 1 = normal lure, 2 = night lure
		packetWriter.writeC(_deceptiveMode); // Fish Deceptive Mode: 0 = no, 1 = yes
		return true;
	}
}