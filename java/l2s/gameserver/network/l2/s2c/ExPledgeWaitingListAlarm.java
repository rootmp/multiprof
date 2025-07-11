package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author GodWorld
 * @reworked by Bonux
 **/
public class ExPledgeWaitingListAlarm implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExPledgeWaitingListAlarm();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}