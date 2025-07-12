package l2s.gameserver.network.l2.s2c.dyee;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExDyeeffectEnchantProbInfo implements IClientOutgoingPacket
{
	private int nCategory;
	private int nSlotID;
	private int nNormalSkillProb;
	private int nHiddenSkillProb;

	public ExDyeeffectEnchantProbInfo()
	{
		System.out.println("NOTDONE " + this.getClass().getSimpleName()); 
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nCategory);
		packetWriter.writeD(nSlotID);
		packetWriter.writeD(nNormalSkillProb);
		packetWriter.writeD(nHiddenSkillProb);
    
		return true;
	}
}
