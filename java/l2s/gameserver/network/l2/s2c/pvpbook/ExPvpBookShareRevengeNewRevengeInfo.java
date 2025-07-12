package l2s.gameserver.network.l2.s2c.pvpbook;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExPvpBookShareRevengeNewRevengeInfo implements IClientOutgoingPacket
{
	private final String killerName, killedName;
	private final int shareType;

	public ExPvpBookShareRevengeNewRevengeInfo(String killedName, String killerName, int shareType)
	{
		this.killedName = killedName;
		this.killerName = killerName;
		this.shareType = shareType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(shareType); // share type
		packetWriter.writeSizedString(killedName);
		packetWriter.writeSizedString(killerName);
		return true;
	}
}
