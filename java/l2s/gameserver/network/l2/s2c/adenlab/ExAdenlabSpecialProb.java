package l2s.gameserver.network.l2.s2c.adenlab;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.adenLab.PkAdenLabSpecialGradeProb;

public class ExAdenlabSpecialProb implements IClientOutgoingPacket
{
	private int nBossID;
	private int nSlotID;
	private PkAdenLabSpecialGradeProb[] gradeProbs;

	public ExAdenlabSpecialProb(int nBossID, int nSlotID, PkAdenLabSpecialGradeProb[] gradeProbs)
	{
		this.nBossID = nBossID;
		this.nSlotID = nSlotID;
		this.gradeProbs = gradeProbs;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nBossID);
		packetWriter.writeD(nSlotID);
		packetWriter.writeD(gradeProbs.length);
		for(int i = 0; i < gradeProbs.length; i++)
		{
			packetWriter.writeD(gradeProbs[i].probs.length);
			for(int probs : gradeProbs[i].probs)
			{
				packetWriter.writeD(probs);
			}
		}
		return true;
	}
}
