package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.pledge.UnitMember;

public class PledgeReceiveMemberInfo implements IClientOutgoingPacket
{
	private UnitMember _member;

	public PledgeReceiveMemberInfo(UnitMember member)
	{
		_member = member;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_member.getPledgeType());
		packetWriter.writeS(_member.getName());
		packetWriter.writeS(_member.getTitle());
		packetWriter.writeD(_member.getPowerGrade());
		packetWriter.writeS(_member.getSubUnit().getName());
		packetWriter.writeS(_member.getRelatedName()); // apprentice/sponsor name if any
		return true;
	}
}