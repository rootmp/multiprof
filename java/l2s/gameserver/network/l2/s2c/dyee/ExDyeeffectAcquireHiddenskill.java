package l2s.gameserver.network.l2.s2c.dyee;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExDyeeffectAcquireHiddenskill implements IClientOutgoingPacket
{
	private int nResult;
	private int nCategory;
	private int nSlotID;
	private int nHiddenSkillID;
	private int nHiddenSkillLevel;

	public ExDyeeffectAcquireHiddenskill()
	{
		System.out.println("NOTDONE " + this.getClass().getSimpleName());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nResult);
		packetWriter.writeD(nCategory);
		packetWriter.writeD(nSlotID);
		packetWriter.writeD(nHiddenSkillID);
		packetWriter.writeD(nHiddenSkillLevel);
		return true;
	}
}
