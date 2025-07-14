package l2s.gameserver.network.l2.s2c.pvpbook;

import java.util.Collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Pvpbook;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExPvpBookShareRevengeList implements IClientOutgoingPacket
{
	private final Collection<PvpbookInfo> pvpbookInfos;

	public ExPvpBookShareRevengeList(Player player)
	{
		Pvpbook pvpbook = player.getPvpbook();
		pvpbookInfos = pvpbook.getInfos(false);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(1); // current page
		packetWriter.writeC(1); // max pages
		packetWriter.writeD(pvpbookInfos.size()); // size

		for(PvpbookInfo pvpbookInfo : pvpbookInfos)
		{
			packetWriter.writeD(pvpbookInfo.isRequestForHelp() == 1 ? 0 : pvpbookInfo.getShareType()); // share type
			packetWriter.writeD(pvpbookInfo.getDeathTime()); // death time
			packetWriter.writeD(pvpbookInfo.getLocationShowCount()); // nShowKillerCount
			packetWriter.writeD(pvpbookInfo.getTeleportCount()); // nTeleportKillerCount
			packetWriter.writeD(pvpbookInfo.getTeleportHelpCount()); // nSharedTeleportKillerCount
			packetWriter.writeD(pvpbookInfo.getKilledObjectId()); // killed user DBID
			packetWriter.writeSizedString(pvpbookInfo.getKilledName()); // killed user name
			packetWriter.writeSizedString(pvpbookInfo.getKilledClanName()); // killed user pledge name
			packetWriter.writeD(pvpbookInfo.getKilledLevel()); // killed user level
			packetWriter.writeD(ClassId.valueOf(pvpbookInfo.getKilledClassId()).getRace().ordinal()); // killed user race
			packetWriter.writeD(pvpbookInfo.getKilledClassId()); // killed user class
			packetWriter.writeD(pvpbookInfo.getKillerObjectId()); // killer id
			packetWriter.writeSizedString(pvpbookInfo.getKillerName()); // killer name
			packetWriter.writeSizedString(pvpbookInfo.getKillerClanName()); // killer clan name
			packetWriter.writeD(pvpbookInfo.getKillerLevel()); // killer level
			packetWriter.writeD(ClassId.valueOf(pvpbookInfo.getKillerClassId()).getRace().ordinal()); // race
			packetWriter.writeD(pvpbookInfo.getKillerClassId()); // class id
			packetWriter.writeD(pvpbookInfo.isOnline() ? 2 : 1); // is online
			packetWriter.writeD(pvpbookInfo.getKarma()); // karma
			packetWriter.writeD(pvpbookInfo.getShareType() == 2 ? pvpbookInfo.getDeathTime() : 0); // shared time
		}
		return true;
	}
}