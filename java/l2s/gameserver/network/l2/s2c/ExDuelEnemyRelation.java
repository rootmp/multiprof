package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExDuelEnemyRelation implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// just trigger
		return true;
	}
}