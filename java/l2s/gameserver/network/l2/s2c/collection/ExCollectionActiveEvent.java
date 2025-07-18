package l2s.gameserver.network.l2.s2c.collection;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.clientDat.CollectionsData;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.CollectionTemplate;

/**
 * @author nexvill
 */
public class ExCollectionActiveEvent implements IClientOutgoingPacket
{
	public ExCollectionActiveEvent()
	{}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		List<CollectionTemplate> collections = CollectionsData.getInstance().getCollectionsByTabId(7);
		packetWriter.writeD(collections.size());
		for(int i = 0; i < collections.size(); i++)
		{
			packetWriter.writeH(collections.get(i).getId()); //event collection id
		}
		return true;
	}
}