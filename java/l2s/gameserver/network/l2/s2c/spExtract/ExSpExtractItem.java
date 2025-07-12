package l2s.gameserver.network.l2.s2c.spExtract;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExSpExtractItem implements IClientOutgoingPacket
{
	private int cResult;
	private int bCritical;
	private int nItemID;

	public ExSpExtractItem(int Result, int Critical, int ItemID)
	{
		cResult = Result;
		bCritical = Critical;
		nItemID = ItemID;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cResult);//cResult
		packetWriter.writeC(bCritical);//bCritical
		packetWriter.writeD(nItemID);//nItemID
		return true;
	}
}