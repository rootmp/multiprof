package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class ExWorldChatCnt implements IClientOutgoingPacket
{
	private final int _count;

	public ExWorldChatCnt(Player player)
	{
		_count = player.getWorldChatPoints();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_count);
		return true;
	}
}
