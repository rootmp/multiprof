package l2s.gameserver.network.l2.s2c.dyee;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExDyeeffectEnchantReset implements IClientOutgoingPacket
{
	private int nResult;
	private int nCategory;
	private int nSlotID;

	public ExDyeeffectEnchantReset()
	{
		System.out.println("NOTDONE " + this.getClass().getSimpleName());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nResult);
		packetWriter.writeD(nCategory);
		packetWriter.writeD(nSlotID);

		return true;
	}
}
