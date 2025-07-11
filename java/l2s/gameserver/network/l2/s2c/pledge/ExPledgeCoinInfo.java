package l2s.gameserver.network.l2.s2c.pledge;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExPledgeCoinInfo implements IClientOutgoingPacket
{
	private final long _count;
	
	public ExPledgeCoinInfo(Player player)
	{
		_count = player.getHonorCoins();
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_count);
		return true;
	}

}
