package l2s.gameserver.network.l2.s2c.randomcraft;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExCraftInfo implements IClientOutgoingPacket
{
	private Player _player;

	public ExCraftInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_player.getCraftPoints());
		packetWriter.writeD(_player.getCraftGaugePoints());
		packetWriter.writeC((_player.getCraftPoints() > 0) ? 1 : 0);// button color (think change if craft points > 0)
		return true;
	}
}