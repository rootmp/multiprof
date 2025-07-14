package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;

public class PartySmallWindowAllPacket implements IClientOutgoingPacket
{
	private final int leaderId;
	private final int loot;
	private final List<PartySmallWindowMemberInfo> members = new ArrayList<PartySmallWindowMemberInfo>();

	public PartySmallWindowAllPacket(Party party, Player leader, Player exclude)
	{
		leaderId = leader.getObjectId();
		loot = party.getLootDistribution();

		for(Player member : party.getPartyMembers())
			if(member != exclude)
				members.add(new PartySmallWindowMemberInfo(member));
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(leaderId); // c3 party leader id
		packetWriter.writeC(loot); // c3 party loot type (0,1,2,....)
		packetWriter.writeC(members.size());
		for(PartySmallWindowMemberInfo mi : members)
		{
			packetWriter.writeD(mi.member.objId);
			packetWriter.writeS(mi.member.name);
			packetWriter.writeD(mi.member.curCp);
			packetWriter.writeD(mi.member.maxCp);
			packetWriter.writeD(mi.member.curHp);
			packetWriter.writeD(mi.member.maxHp);
			packetWriter.writeD(mi.member.curMp);
			packetWriter.writeD(mi.member.maxMp);
			packetWriter.writeD(mi.member.sayhas_grace); // Sayha's Grace Points
			packetWriter.writeC(mi.member.level);
			packetWriter.writeH(mi.member.classId);
			packetWriter.writeC(mi.member.sex);
			packetWriter.writeH(mi.member.raceId);
			packetWriter.writeD(0);
			packetWriter.writeD(mi.m_servitors.size()); // Pet Count
			for(PartyMember servitor : mi.m_servitors)
			{
				packetWriter.writeD(servitor.objId);
				packetWriter.writeD(servitor.npcId);
				packetWriter.writeC(servitor.type);
				packetWriter.writeS(servitor.name);
				packetWriter.writeD(servitor.curHp);
				packetWriter.writeD(servitor.maxHp);
				packetWriter.writeD(servitor.curMp);
				packetWriter.writeD(servitor.maxMp);
				packetWriter.writeC(servitor.level);
			}
		}
		return true;
	}

	public static class PartySmallWindowMemberInfo
	{
		public PartyMember member;
		public List<PartyMember> m_servitors;

		public PartySmallWindowMemberInfo(Player player)
		{
			member = new PartyMember();
			member.name = player.getName();
			member.objId = player.getObjectId();
			member.curCp = (int) player.getCurrentCp();
			member.maxCp = player.getMaxCp();
			member.curHp = (int) player.getCurrentHp();
			member.maxHp = player.getMaxHp();
			member.curMp = (int) player.getCurrentMp();
			member.maxMp = player.getMaxMp();
			member.level = player.getLevel();
			member.classId = player.getClassId().getId();
			member.raceId = player.getRace().ordinal();
			member.sayhas_grace = player.getSayhasGrace();
			member.sex = player.getSex().ordinal();
			member.isPartySubstituteStarted = player.isPartySubstituteStarted() ? 1 : 0;

			m_servitors = new ArrayList<PartyMember>();

			for(Servitor s : player.getServitors())
			{
				PartyMember m_servitor = new PartyMember();
				m_servitor.name = s.getName();
				m_servitor.objId = s.getObjectId();
				m_servitor.npcId = s.getNpcId() + 1000000;
				m_servitor.curHp = (int) s.getCurrentHp();
				m_servitor.maxHp = s.getMaxHp();
				m_servitor.curMp = (int) s.getCurrentMp();
				m_servitor.maxMp = s.getMaxMp();
				m_servitor.level = s.getLevel();
				m_servitor.type = s.getServitorType();
				m_servitors.add(m_servitor);
			}
		}
	}

	public static class PartyMember
	{
		public String name;
		public int objId;
		public int npcId;
		public int curCp;
		public int maxCp;
		public int curHp;
		public int maxHp;
		public int curMp;
		public int maxMp;
		public int level;
		public int classId;
		public int raceId;
		public int sayhas_grace;
		public int type;
		public int sex;
		public int isPartySubstituteStarted;
	}
}