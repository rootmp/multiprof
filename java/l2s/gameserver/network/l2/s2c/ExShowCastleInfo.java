package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.tables.ClanTable;

public class ExShowCastleInfo implements IClientOutgoingPacket
{
	private List<CastleInfo> _infos = Collections.emptyList();

	public ExShowCastleInfo()
	{
		String ownerName;
		int id, tax, nextSiege, side;
		boolean inSiege;

		List<Castle> castles = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		_infos = new ArrayList<CastleInfo>(castles.size());
		for (Castle castle : castles)
		{
			ownerName = ClanTable.getInstance().getClanName(castle.getOwnerId());
			id = castle.getId();
			tax = castle.getTaxPercent();
			side = castle.getResidenceSide().ordinal();
			if (castle.getSiegeEvent() != null)
			{
				nextSiege = (int) (castle.getSiegeDate().getTimeInMillis() / 1000);
				inSiege = castle.getSiegeEvent().isInProgress();
			}
			else
			{
				nextSiege = 0;
				inSiege = false;
			}
			_infos.add(new CastleInfo(ownerName, id, tax, nextSiege, inSiege, side));
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// temp fix for world map, while castle siege only giran
		// packetWriter.writeD(_infos.size());
		packetWriter.writeD(6);
		for (int i = 1; i < 3; i++)
		{
			packetWriter.writeD(i);
			packetWriter.writeS(StringUtils.EMPTY);
			packetWriter.writeD(0);
			packetWriter.writeD(0);
			packetWriter.writeC(0);
			packetWriter.writeC(0);
		}
		for (CastleInfo info : _infos)
		{
			packetWriter.writeD(info._id);
			packetWriter.writeS(info._ownerName);
			packetWriter.writeD(info._tax);
			packetWriter.writeD(info._nextSiege);
			packetWriter.writeC(info._inSiege);
			packetWriter.writeC(info._side);
		}
		_infos.clear();
		for (int i = 4; i < 7; i++)
		{
			packetWriter.writeD(i);
			packetWriter.writeS(StringUtils.EMPTY);
			packetWriter.writeD(0);
			packetWriter.writeD(0);
			packetWriter.writeC(0);
			packetWriter.writeC(0);
		}
	}

	private static class CastleInfo
	{
		public String _ownerName;
		public int _id, _tax, _nextSiege, _side;
		public boolean _inSiege;

		public CastleInfo(String ownerName, int id, int tax, int nextSiege, boolean inSiege, int side)
		{
			_ownerName = ownerName;
			_id = id;
			_tax = tax;
			_nextSiege = nextSiege;
			_inSiege = inSiege;
			_side = side;
		}
	}
}