package l2s.gameserver.network.l2.s2c.pvpbook;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Pvpbook;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPvpBookShareRevengeList extends L2GameServerPacket
{
	private final int locationShowCount;
	private final int teleportCount;
	private final Collection<PvpbookInfo> pvpbookInfos;
	private final Player _player;

	public ExPvpBookShareRevengeList(Player player)
	{
		Pvpbook pvpbook = player.getPvpbook();
		locationShowCount = pvpbook.getLocationShowCount();
		teleportCount = pvpbook.getTeleportCount();
		pvpbookInfos = pvpbook.getInfos(false);
		_player = player;
	}

	@Override
	public void writeImpl()
	{
		writeC(1); // current page
		writeC(1); // max pages
		writeD(pvpbookInfos.size()); // size

		int shareType = 1;
		for (PvpbookInfo pvpbookInfo : pvpbookInfos)
		{
			if (pvpbookInfo.getKilledObjectId() != _player.getObjectId())
			{
				shareType = 2;
			}

			writeD(shareType); // share type
			writeD(pvpbookInfo.getDeathTime()); // death time
			writeD(locationShowCount); // nShowKillerCount
			writeD(teleportCount); // nTeleportKillerCount
			writeD(1); // nSharedTeleportKillerCount
			writeD(pvpbookInfo.getKilledObjectId()); // killed user DBID
			writeString(pvpbookInfo.getKilledName()); // killed user name
			writeString(pvpbookInfo.getKilledClanName()); // killed user pledge name
			writeD(pvpbookInfo.getKilledLevel()); // killed user level
			writeD(ClassId.valueOf(pvpbookInfo.getKilledClassId()).getRace().ordinal()); // killed user race
			writeD(pvpbookInfo.getKilledClassId()); // killed user class
			writeD(pvpbookInfo.getKillerObjectId()); // killer id
			writeString(pvpbookInfo.getKillerName()); // killer name
			writeString(pvpbookInfo.getKillerClanName()); // killer clan name
			writeD(pvpbookInfo.getKillerLevel()); // killer level
			writeD(ClassId.valueOf(pvpbookInfo.getKillerClassId()).getRace().ordinal()); // race
			writeD(pvpbookInfo.getKillerClassId()); // class id
			writeD(pvpbookInfo.isOnline() ? 2 : 1); // is online
			writeD(pvpbookInfo.getKarma()); // karma
			writeD(shareType == 2 ? pvpbookInfo.getDeathTime() : 0); // shared time
		}
	}
}