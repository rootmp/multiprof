package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.pledge.RankPrivs;

public class PledgePowerGradeList implements IClientOutgoingPacket
{
	private RankPrivs[] _privs;

	public PledgePowerGradeList(RankPrivs[] privs)
	{
		_privs = privs;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_privs.length);
		for(RankPrivs element : _privs)
		{
			packetWriter.writeD(element.getRank());
			packetWriter.writeD(element.getParty());
		}
		return true;
	}
}