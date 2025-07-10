package l2s.gameserver.network.l2.s2c.subjugation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class ExSubjugationRanking extends L2GameServerPacket
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
	protected final void writeImpl()
	{
		if (_result.size() > 5)
			writeD(5);
		else
			writeD(_result.size());

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
				writeD(player.getInteger("points"));
				writeD(id); // rank
			}
		}
		writeD(_zoneId);
		writeD(points);
		writeD(rank);
	}
}