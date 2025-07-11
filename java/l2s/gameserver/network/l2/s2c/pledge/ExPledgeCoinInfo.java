package l2s.gameserver.network.l2.s2c.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 **/
public class ExPledgeCoinInfo implements IClientOutgoingPacket
{
	private final Player _player;

	public ExPledgeCoinInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_player.getHonorCoins());
		return true;
	}
}