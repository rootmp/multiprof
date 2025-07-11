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
public class ExPvpBookShareRevengeList implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(1); // current page
		packetWriter.writeC(1); // max pages
		packetWriter.writeD(pvpbookInfos.size()); // size

		int shareType = 1;
		for (PvpbookInfo pvpbookInfo : pvpbookInfos)
		{
			if (pvpbookInfo.getKilledObjectId() != _player.getObjectId())
			{
				shareType = 2;
			}

			packetWriter.writeD(shareType); // share type
			packetWriter.writeD(pvpbookInfo.getDeathTime()); // death time
			packetWriter.writeD(locationShowCount); // nShowKillerCount
			packetWriter.writeD(teleportCount); // nTeleportKillerCount
			packetWriter.writeD(1); // nSharedTeleportKillerCount
			packetWriter.writeD(pvpbookInfo.getKilledObjectId()); // killed user DBID
			packetWriter.writeString(pvpbookInfo.getKilledName()); // killed user name
			packetWriter.writeString(pvpbookInfo.getKilledClanName()); // killed user pledge name
			packetWriter.writeD(pvpbookInfo.getKilledLevel()); // killed user level
			packetWriter.writeD(ClassId.valueOf(pvpbookInfo.getKilledClassId()).getRace().ordinal()); // killed user race
			packetWriter.writeD(pvpbookInfo.getKilledClassId()); // killed user class
			packetWriter.writeD(pvpbookInfo.getKillerObjectId()); // killer id
			packetWriter.writeString(pvpbookInfo.getKillerName()); // killer name
			packetWriter.writeString(pvpbookInfo.getKillerClanName()); // killer clan name
			packetWriter.writeD(pvpbookInfo.getKillerLevel()); // killer level
			packetWriter.writeD(ClassId.valueOf(pvpbookInfo.getKillerClassId()).getRace().ordinal()); // race
			packetWriter.writeD(pvpbookInfo.getKillerClassId()); // class id
			packetWriter.writeD(pvpbookInfo.isOnline() ? 2 : 1); // is online
			packetWriter.writeD(pvpbookInfo.getKarma()); // karma
			packetWriter.writeD(shareType == 2 ? pvpbookInfo.getDeathTime() : 0); // shared time
		}
		return true;
	}
}