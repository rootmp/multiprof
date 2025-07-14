package l2s.gameserver.network.l2.s2c.subjugation;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExSubjugationSidebar implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		int points = 0;
		if(_zoneId == 0)
		{
			for(int i = 1; i < 8; i++)
			{
				if(_player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + i, 0) > points)
				{
					points = _player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + i);
					_zoneId = i;
				}
			}

			if(points == 0)
			{ return false; }
		}
		else
		{
			points = _player.getVarInt(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + _zoneId);
		}

		points = points % 1000000;
		int keys = points / 1000000;

		packetWriter.writeD(_zoneId);
		packetWriter.writeD(points);
		packetWriter.writeD(keys);
		return true;
	}
}