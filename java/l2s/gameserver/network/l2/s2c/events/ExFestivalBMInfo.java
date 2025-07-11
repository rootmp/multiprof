package l2s.gameserver.network.l2.s2c.events;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExFestivalBMInfo implements IClientOutgoingPacket
{
	private final Player _player;

	public ExFestivalBMInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(Config.BM_FESTIVAL_ITEM_TO_PLAY);

		if (Config.BM_FESTIVAL_PLAY_LIMIT != -1)
		{
			packetWriter.writeQ(_player.getVarInt("FESTIVAL_BM_EXIST_GAMES", Config.BM_FESTIVAL_PLAY_LIMIT));
		}
		else
		{
			packetWriter.writeQ(0);
		}

		packetWriter.writeD(Config.BM_FESTIVAL_ITEM_TO_PLAY_COUNT);
		return true;
	}
}