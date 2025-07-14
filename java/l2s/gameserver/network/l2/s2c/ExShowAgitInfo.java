package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.entity.residence.clanhall.NormalClanHall;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.tables.ClanTable;

public class ExShowAgitInfo implements IClientOutgoingPacket
{
	private final List<AgitInfo> _infos;

	public ExShowAgitInfo()
	{
		List<NormalClanHall> clanHalls = ResidenceHolder.getInstance().getResidenceList(NormalClanHall.class);

		_infos = new ArrayList<AgitInfo>(clanHalls.size());

		clanHalls.forEach(clanHall -> {
			int ch_id = clanHall.getId();
			int getType = clanHall.getClanHallType().ordinal();
			Clan clan = ClanTable.getInstance().getClan(clanHall.getOwnerId());
			String clan_name = (clanHall.getOwnerId() == 0) || (clan == null) ? StringUtils.EMPTY : clan.getName();
			String leader_name = (clanHall.getOwnerId() == 0) || (clan == null) ? StringUtils.EMPTY : clan.getLeaderName();
			_infos.add(new AgitInfo(clan_name, leader_name, ch_id, getType));
		});
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_infos.size());
		_infos.forEach(info -> {
			packetWriter.writeD(info.ch_id);
			packetWriter.writeS(info.clan_name);
			packetWriter.writeS(info.leader_name);
			packetWriter.writeD(info.getType);
		});

		return true;
	}

	static class AgitInfo
	{
		public String clan_name, leader_name;
		public int ch_id, getType;

		public AgitInfo(String clan_name, String leader_name, int ch_id, int lease)
		{
			this.clan_name = clan_name;
			this.leader_name = leader_name;
			this.ch_id = ch_id;
			this.getType = lease;
		}
	}
}