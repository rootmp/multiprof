package l2s.gameserver.network.l2.s2c.randomcraft;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExCraftRandomMake implements IClientOutgoingPacket
{
	private final int nItemClassID;
	private final long nAmount;
	private int cResult;
	private int cEnchanted;
	
	public ExCraftRandomMake(int itemId, long itemCount, int enchanted)
	{
		cResult = 0;
		nItemClassID = itemId;
		nAmount = itemCount;
		cEnchanted= enchanted;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cResult);
		packetWriter.writeH(15); // size
		packetWriter.writeD(nItemClassID);
		packetWriter.writeQ(nAmount);
		packetWriter.writeC(cEnchanted);
		return true;
	}
}
