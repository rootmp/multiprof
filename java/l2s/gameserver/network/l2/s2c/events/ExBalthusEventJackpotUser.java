package l2s.gameserver.network.l2.s2c.events;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExBalthusEventJackpotUser implements IClientOutgoingPacket
{
	public ExBalthusEventJackpotUser()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(1);
		return true;
	}
}