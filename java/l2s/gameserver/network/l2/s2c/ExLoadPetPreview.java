package l2s.gameserver.network.l2.s2c;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.pet.PetSkillPreview;

public class ExLoadPetPreview implements IClientOutgoingPacket
{
	private int nPetCollarServerId;
	private int nPetCollarDBId;
	private int nPetIndex;
	private long nPetExp;
	private long nMinExp;
	private long nMaxExp;
	private List<PetSkillPreview> acquireSkillList;
	private int nNPCClassID;
	private int nEvolveShapeID;

	public ExLoadPetPreview()
	{
		System.out.println("NOTDONE " + this.getClass().getSimpleName());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nPetCollarServerId);
		packetWriter.writeD(nPetCollarDBId);
		packetWriter.writeD(nPetIndex);
		packetWriter.writeQ(nPetExp);
		packetWriter.writeQ(nMinExp);
		packetWriter.writeQ(nMaxExp);
		packetWriter.writeD(acquireSkillList.size());
		for(PetSkillPreview skill : acquireSkillList)
		{
			packetWriter.writeD(skill.nSkillID);
			packetWriter.writeD(skill.nSkillLv);
			packetWriter.writeC(skill.bSkillEnchant);
			packetWriter.writeC(skill.bSkillLock);
		}
		packetWriter.writeD(nNPCClassID);
		packetWriter.writeD(nEvolveShapeID);
		return true;
	}
}
