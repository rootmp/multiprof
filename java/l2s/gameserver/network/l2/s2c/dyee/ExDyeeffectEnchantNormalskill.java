package l2s.gameserver.network.l2.s2c.dyee;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExDyeeffectEnchantNormalskill implements IClientOutgoingPacket
{
	private int nResult;
	private int nCategory;
	private int nSlotID;
	private int nSlotLevel;
	private int nSkillID;
	private int nSkillLevel;
	private int nChallengeCount;

	public ExDyeeffectEnchantNormalskill()
	{
		System.out.println("NOTDONE " + this.getClass().getSimpleName()); 
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
    packetWriter.writeD(nResult);
    packetWriter.writeD(nCategory);
    packetWriter.writeD(nSlotID);
    packetWriter.writeD(nSlotLevel);
    packetWriter.writeD(nSkillID);
    packetWriter.writeD(nSkillLevel);
    packetWriter.writeD(nChallengeCount);

		return true;
	}
}
