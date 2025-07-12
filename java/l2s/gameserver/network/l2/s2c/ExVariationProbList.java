package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.VariationDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.support.variation.VariationProb;

public class ExVariationProbList implements IClientOutgoingPacket
{
	private int nRefineryID;
	private int nTargetItemId;
	private List<VariationProb> variationProbList = new ArrayList<>();
	private int nCurrentPage;
	private int nMaxPage;

	public ExVariationProbList(Player player, int nRefineryID, int nTargetItemId)
	{
		this.nRefineryID = nRefineryID;
		this.nTargetItemId = nTargetItemId;
		nCurrentPage = 1;
		nMaxPage = 1;
		variationProbList = VariationDataHolder.getInstance().findVariationProbs(nRefineryID, nTargetItemId);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nRefineryID);
		packetWriter.writeD(nTargetItemId);
		packetWriter.writeD(nCurrentPage);
		packetWriter.writeD(nMaxPage);

		packetWriter.writeD(variationProbList.size());
		for(VariationProb v : variationProbList)
		{
			packetWriter.writeD(v.nOptionCategory);
			packetWriter.writeD(v.nOptionID);
			packetWriter.writeQ(v.nProb);
		}
		return true;
	}
}