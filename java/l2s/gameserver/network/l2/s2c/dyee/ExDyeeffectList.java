package l2s.gameserver.network.l2.s2c.dyee;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.DyeEffectInfo;

public class ExDyeeffectList implements IClientOutgoingPacket
{

	private List<DyeEffectInfo> vDyeEffectInfoList;

	public ExDyeeffectList()
	{
		System.out.println("NOTDONE " + this.getClass().getSimpleName()); 
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(vDyeEffectInfoList.size());
		for(DyeEffectInfo effect : vDyeEffectInfoList)
		{
			packetWriter.writeD(effect.nCategory);
			packetWriter.writeD(effect.nSlotID);
			packetWriter.writeD(effect.nSlotLevel);
			packetWriter.writeD(effect.nSkillID);
			packetWriter.writeD(effect.nSkillLevel);
			packetWriter.writeD(effect.nHiddenSkillID);
			packetWriter.writeD(effect.nHiddenSkillLevel);
	    packetWriter.writeD(effect.nChallengeCount);
		}
		return true;
	}
}
