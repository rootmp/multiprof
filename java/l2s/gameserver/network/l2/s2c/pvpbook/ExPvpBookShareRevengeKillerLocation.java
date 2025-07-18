package l2s.gameserver.network.l2.s2c.pvpbook;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.ILocation;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExPvpBookShareRevengeKillerLocation implements IClientOutgoingPacket
{
	private final String killerName;
	private final ILocation killerLoc;

	public ExPvpBookShareRevengeKillerLocation(String killerName, ILocation killerLoc)
	{
		this.killerName = killerName;
		this.killerLoc = killerLoc;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeSizedString(killerName);
		packetWriter.writeD(killerLoc.getX());
		packetWriter.writeD(killerLoc.getY());
		packetWriter.writeD(killerLoc.getZ());
		return true;
	}
}