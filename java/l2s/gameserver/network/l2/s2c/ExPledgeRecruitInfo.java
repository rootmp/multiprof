package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.SubUnit;

/**
 * @author GodWorld
 * @reworked by Bonux
 **/
public class ExPledgeRecruitInfo implements IClientOutgoingPacket
{
	private final String _clanName;
	private final String _leaderName;
	private final int _clanLevel;
	private final int _clanMemberCount;
	private final List<SubUnit> _subUnits = new ArrayList<SubUnit>();

	public ExPledgeRecruitInfo(Clan clan)
	{
		_clanName = clan.getName();
		_leaderName = clan.getLeader().getName();
		_clanLevel = clan.getLevel();
		_clanMemberCount = clan.getAllSize();

		for (SubUnit su : clan.getAllSubUnits())
		{
			if (su.getType() == Clan.SUBUNIT_MAIN_CLAN)
			{
				continue;
			}

			_subUnits.add(su);
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_clanName);
		packetWriter.writeS(_leaderName);
		packetWriter.writeD(_clanLevel);
		packetWriter.writeD(_clanMemberCount);
		packetWriter.writeD(_subUnits.size());
		for (SubUnit su : _subUnits)
		{
			packetWriter.writeD(su.getType());
			packetWriter.writeS(su.getName());
		}
		return true;
	}
}