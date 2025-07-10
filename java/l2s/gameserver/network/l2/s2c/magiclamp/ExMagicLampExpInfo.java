package l2s.gameserver.network.l2.s2c.magiclamp;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExMagicLampExpInfo extends L2GameServerPacket
{
	private Player _player;
	private int MAX_LAMP_EXP = Config.MAGIC_LAMP_MAX_LEVEL_EXP;

	public ExMagicLampExpInfo(Player player)
	{
		_player = player;
	}

	@Override
	protected final void writeImpl()
	{
		int currentPoints = 0;
		int lampsExist = 0;
		if ((_player.getMagicLampPoints() % MAX_LAMP_EXP) > 0)
		{
			currentPoints = (int) (_player.getMagicLampPoints() % MAX_LAMP_EXP);
		}
		if ((_player.getMagicLampPoints() / MAX_LAMP_EXP) >= 1)
		{
			lampsExist = (int) (_player.getMagicLampPoints() / MAX_LAMP_EXP);
		}
		writeD(Config.MAGIC_LAMP_ENABLED ? 1 : 0);
		writeD(MAX_LAMP_EXP); // points to gain 1 lamp
		writeD(currentPoints); // current points
		writeD(lampsExist); // lamps exist
	}
}