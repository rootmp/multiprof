package l2s.gameserver.network.l2.s2c.prot_507;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.item.BlessOptionProb;

public class ExBlessOptionProbList implements IClientOutgoingPacket
{
	private int nScrollClassID;
	private int nItemClassID;
	private List<BlessOptionProb> optionProbList;
	private List<BlessOptionProb> shapeProbList;

	public ExBlessOptionProbList(int nScrollClassID, int nItemClassID, List<BlessOptionProb> optionProbList, List<BlessOptionProb> shapeProbList)
	{
		this.nScrollClassID = nScrollClassID;
		this.nItemClassID = nItemClassID;
		this.optionProbList = optionProbList;
		this.shapeProbList = shapeProbList;
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nScrollClassID);
		packetWriter.writeD(nItemClassID);

		packetWriter.writeD(optionProbList.size());
		for(BlessOptionProb prob : optionProbList)
		{
			packetWriter.writeD(prob.nID);
			packetWriter.writeD(prob.nProb);
		}

		packetWriter.writeD(shapeProbList.size());
		for(BlessOptionProb prob : shapeProbList)
		{
			packetWriter.writeD(prob.nID);
			packetWriter.writeD(prob.nProb);
		}
		return true;
	}
}
