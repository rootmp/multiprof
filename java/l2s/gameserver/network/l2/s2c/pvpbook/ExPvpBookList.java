package l2s.gameserver.network.l2.s2c.pvpbook;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Pvpbook;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExPvpBookList extends L2GameServerPacket
{
	private final int locationShowCount;
	private final int teleportCount;
	private final Collection<PvpbookInfo> pvpbookInfos;

	public ExPvpBookList(Player player)
	{
		Pvpbook pvpbook = player.getPvpbook();
		locationShowCount = pvpbook.getLocationShowCount();
		teleportCount = pvpbook.getTeleportCount();
		pvpbookInfos = pvpbook.getInfos(false);
	}

	@Override
	public void writeImpl()
	{
		writeD(locationShowCount); // Show locations count
		writeD(teleportCount); // Teleports count
		writeD(pvpbookInfos.size());
		for (PvpbookInfo pvpbookInfo : pvpbookInfos)
		{
			writeString(pvpbookInfo.getKillerName()); // Char name
			writeString(pvpbookInfo.getKillerClanName()); // Clan name
			writeD(pvpbookInfo.getKillerLevel()); // Level
			writeD(ClassId.valueOf(pvpbookInfo.getKillerClassId()).getRace().ordinal()); // Race
			writeD(pvpbookInfo.getKillerClassId()); // Class ID
			writeD(pvpbookInfo.getDeathTime()); // Death time
			writeC(pvpbookInfo.isOnline()); // Online
		}
	}
}