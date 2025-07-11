package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author monithly
 */
public class ExCheck_SpeedHack implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}
