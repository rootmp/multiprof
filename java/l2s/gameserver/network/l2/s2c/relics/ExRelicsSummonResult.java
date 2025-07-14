package l2s.gameserver.network.l2.s2c.relics;

import java.util.Collections;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRelicsSummonResult implements IClientOutgoingPacket
{
	public static final ExRelicsSummonResult FAIL = new ExRelicsSummonResult();

	private int cResult;
	private int nItemID;
	private List<Integer> relicsList;

	public ExRelicsSummonResult()
	{
		this.relicsList = Collections.emptyList();
	}

	public ExRelicsSummonResult(int cResult, int nItemID, List<Integer> relicsList)
	{
		this.cResult = cResult;
		this.nItemID = nItemID;
		this.relicsList = relicsList;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cResult);
		packetWriter.writeD(nItemID);

		packetWriter.writeD(relicsList.size());
		for(int relic : relicsList)
		{
			packetWriter.writeD(relic);
		}
		return true;
	}
}
