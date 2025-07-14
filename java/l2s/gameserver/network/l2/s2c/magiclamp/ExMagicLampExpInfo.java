package l2s.gameserver.network.l2.s2c.magiclamp;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExMagicLampExpInfo implements IClientOutgoingPacket
{
	private Player _player;
	private int MAX_LAMP_EXP = Config.MAGIC_LAMP_MAX_LEVEL_EXP;

	public ExMagicLampExpInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		int currentPoints = 0;
		int lampsExist = 0;
		if((_player.getMagicLampPoints() % MAX_LAMP_EXP) > 0)
		{
			currentPoints = (int) (_player.getMagicLampPoints() % MAX_LAMP_EXP);
		}
		if((_player.getMagicLampPoints() / MAX_LAMP_EXP) >= 1)
		{
			lampsExist = (int) (_player.getMagicLampPoints() / MAX_LAMP_EXP);
		}
		packetWriter.writeD(Config.MAGIC_LAMP_ENABLED ? 1 : 0);
		packetWriter.writeD(MAX_LAMP_EXP); // points to gain 1 lamp
		packetWriter.writeD(currentPoints); // current points
		packetWriter.writeD(lampsExist); // lamps exist
		return true;
	}
}