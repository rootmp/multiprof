package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;

public class PartySmallWindowAddPacket implements IClientOutgoingPacket
{
	private final int _leaderObjectId, _loot;
	private final PartySmallWindowAllPacket.PartyMember _member;

	public PartySmallWindowAddPacket(Player player, Player member)
	{
		_leaderObjectId = member.getParty().getPartyLeader().getObjectId();
		_loot = member.getParty().getLootDistribution();
		_member = new PartySmallWindowAllPacket.PartySmallWindowMemberInfo(member).member;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_leaderObjectId);
		packetWriter.writeD(_loot);
		packetWriter.writeD(_member.objId);
		packetWriter.writeS(_member.name);
		packetWriter.writeD(_member.curCp);
		packetWriter.writeD(_member.maxCp);
		packetWriter.writeD(_member.curHp);
		packetWriter.writeD(_member.maxHp);
		packetWriter.writeD(_member.curMp);
		packetWriter.writeD(_member.maxMp);
		packetWriter.writeD(_member.sayhas_grace); // Sayha's Grace Points
		packetWriter.writeC(_member.level);
		packetWriter.writeH(_member.classId);
		packetWriter.writeC(_member.sex);
		packetWriter.writeH(_member.raceId);
		packetWriter.writeD(0);
		return true;
	}
}