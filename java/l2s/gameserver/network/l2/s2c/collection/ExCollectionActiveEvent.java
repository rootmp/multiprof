package l2s.gameserver.network.l2.s2c.collection;

import java.util.List;

import l2s.gameserver.data.xml.holder.CollectionsHolder;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.CollectionTemplate;

/**
 * @author nexvill
 */
public class ExCollectionActiveEvent extends L2GameServerPacket
{
	public ExCollectionActiveEvent()
	{
	}

	@Override
	protected final void writeImpl()
	{
		List<CollectionTemplate> collections = CollectionsHolder.getInstance().getCollectionsByTabId(7);
		writeD(collections.size());
		for (int i = 0; i < collections.size(); i++)
		{
			writeH(collections.get(i).getId()); // event collection id
		}
	}
}