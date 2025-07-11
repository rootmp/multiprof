package l2s.gameserver.network.l2.s2c.relics;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.relics.CollectionRelicsInfo;
import l2s.gameserver.templates.relics.RelicsCollection;

public class ExRelicsCollectionInfo implements IClientOutgoingPacket
{
	private int nIndex;
	private int nMaxIndex;
	private List<RelicsCollection> collectionList;

	public ExRelicsCollectionInfo(int nIndex, int nMaxIndex, List<RelicsCollection> collectionList)
	{
		this.nIndex = nIndex;
		this.nMaxIndex = nMaxIndex;
		this.collectionList = collectionList;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nIndex);
		packetWriter.writeD(nMaxIndex);

		packetWriter.writeD(collectionList.size());
		for(RelicsCollection coll : collectionList)
		{
			packetWriter.writeD(coll.nCollectionID);
			packetWriter.writeC(coll.bComplete);
			packetWriter.writeD(coll.relicsList.size());
			for(CollectionRelicsInfo relic : coll.relicsList)
			{
				packetWriter.writeD(relic.nRelicsID);
				packetWriter.writeD(relic.nLevel);
			}

		}
		return true;
	}
}