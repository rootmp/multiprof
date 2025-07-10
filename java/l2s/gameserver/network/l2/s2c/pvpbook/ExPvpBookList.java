package l2s.gameserver.network.l2.s2c.pvpbook;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Pvpbook;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExPvpBookList implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(locationShowCount); // Show locations count
		packetWriter.writeD(teleportCount); // Teleports count
		packetWriter.writeD(pvpbookInfos.size());
		for (PvpbookInfo pvpbookInfo : pvpbookInfos)
		{
			packetWriter.writeString(pvpbookInfo.getKillerName()); // Char name
			packetWriter.writeString(pvpbookInfo.getKillerClanName()); // Clan name
			packetWriter.writeD(pvpbookInfo.getKillerLevel()); // Level
			packetWriter.writeD(ClassId.valueOf(pvpbookInfo.getKillerClassId()).getRace().ordinal()); // Race
			packetWriter.writeD(pvpbookInfo.getKillerClassId()); // Class ID
			packetWriter.writeD(pvpbookInfo.getDeathTime()); // Death time
			packetWriter.writeC(pvpbookInfo.isOnline()); // Online
		}
	}
}