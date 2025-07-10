package l2s.gameserver.network.l2.s2c.subjugation;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExSubjugationSidebar extends L2GameServerPacket
{
	private final Player _player;
	private int _zoneId;

	public ExSubjugationSidebar(Player player)
	{
		_player = player;
		_zoneId = 0;
	}

	public ExSubjugationSidebar(Player player, int zoneId)
	{
		_player = player;
		_zoneId = zoneId;
	}

	@Override
	protected final void writeImpl()
	{
		int points = 0;
		if (_zoneId == 0)
		{
			for (int i = 1; i < 8; i++)
			{
				if (_player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + i, 0) > points)
				{
					points = _player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + i);
					_zoneId = i;
				}
			}

			if (points == 0)
			{
				return;
			}
		}
		else
		{
			points = _player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + _zoneId);
		}

		points = points % 1000000;
		int keys = points / 1000000;

		writeD(_zoneId);
		writeD(points);
		writeD(keys);
	}
}