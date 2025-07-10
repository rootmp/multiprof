package l2s.gameserver.network.l2.s2c.subjugation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class ExSubjugationRanking implements IClientOutgoingPacket
{
	private Player _player;
	private int _zoneId;
	private Map<Integer, StatsSet> _result = new ConcurrentHashMap<>();

	public ExSubjugationRanking(Player player, int zone, Map<Integer, StatsSet> res)
	{
		_player = player;
		_zoneId = zone;
		_result = res;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (_result.size() > 5)
			packetWriter.writeD(5);
		else
			packetWriter.writeD(_result.size());

		int i = 0;
		int points = 0;
		int rank = 0;
		for (int id : _result.keySet())
		{
			StatsSet player = new StatsSet();
			player = _result.get(id);
			i++;
			if (player.getInteger("charId") == _player.getObjectId())
			{
				points = player.getInteger("points");
				rank = i;
			}

			if (id < 6)
			{
				writeString(player.getString("name"));
				packetWriter.writeD(player.getInteger("points"));
				packetWriter.writeD(id); // rank
			}
		}
		packetWriter.writeD(_zoneId);
		packetWriter.writeD(points);
		packetWriter.writeD(rank);
	}
}