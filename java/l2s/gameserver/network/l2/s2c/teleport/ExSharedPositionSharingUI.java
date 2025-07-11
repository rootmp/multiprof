package l2s.gameserver.network.l2.s2c.teleport;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExSharedPositionSharingUI implements IClientOutgoingPacket
{
	private Player _player;

	public ExSharedPositionSharingUI(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if ((_player.getPreviousPvpRank() > 0) && (_player.getPreviousPvpRank() < 4))
			packetWriter.writeQ(0);
		else
			packetWriter.writeQ(Config.SHARE_POSITION_COST);
		
		return true;
	}
}