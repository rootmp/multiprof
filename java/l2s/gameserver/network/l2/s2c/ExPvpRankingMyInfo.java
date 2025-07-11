package l2s.gameserver.network.l2.s2c;

import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class ExPvpRankingMyInfo implements IClientOutgoingPacket
{
	private final Player _player;
	private final Map<Integer, StatsSet> _playerList;

	public ExPvpRankingMyInfo(Player player)
	{
		_player = player;
		_playerList = RankManager.getInstance().getPvpRankList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (_playerList.size() > 0)
		{
			boolean found = false;
			for (Integer id : _playerList.keySet())
			{
				final StatsSet player = _playerList.get(id);
				if (player.getInteger("charId") == _player.getObjectId())
				{
					found = true;
					packetWriter.writeQ(player.getInteger("points")); // pvp points
					packetWriter.writeD(id); // current rank
					packetWriter.writeD(id); // ingame shown change in rank as this value - current rank value.
					packetWriter.writeD(player.getInteger("kills")); // kills
					packetWriter.writeD(player.getInteger("deaths")); // deaths
				}
			}
			if (!found)
			{
				packetWriter.writeQ(0); // pvp points
				packetWriter.writeD(0); // current rank
				packetWriter.writeD(0); // ingame shown change in rank as this value - current rank value.
				packetWriter.writeD(0); // kills
				packetWriter.writeD(0); // deaths
			}
		}
		else
		{
			packetWriter.writeQ(0); // pvp points
			packetWriter.writeD(0); // current rank
			packetWriter.writeD(0); // ingame shown change in rank as this value - current rank value.
			packetWriter.writeD(0); // kills
			packetWriter.writeD(0); // deaths
		}
		return true;
	}
}