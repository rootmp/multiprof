package l2s.gameserver.network.l2.s2c.pvpbook;

import l2s.gameserver.geometry.ILocation;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPvpBookShareRevengeKillerLocation extends L2GameServerPacket
{
	private final String killerName;
	private final ILocation killerLoc;

	public ExPvpBookShareRevengeKillerLocation(String killerName, ILocation killerLoc)
	{
		this.killerName = killerName;
		this.killerLoc = killerLoc;
	}

	@Override
	public void writeImpl()
	{
		writeString(killerName);
		writeD(killerLoc.getX());
		writeD(killerLoc.getY());
		writeD(killerLoc.getZ());
	}
}