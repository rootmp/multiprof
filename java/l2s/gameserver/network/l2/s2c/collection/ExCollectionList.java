package l2s.gameserver.network.l2.s2c.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.gameserver.data.xml.holder.CollectionsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.CollectionTemplate;

/**
 * @author nexvill packet used show only active event collections, that player
 *         have otherwise just send tabId and count = 0
 */
public class ExCollectionList extends L2GameServerPacket
{
	private Player _player;
	private int _tabId;

	public ExCollectionList(Player player, int tabId)
	{
		_player = player;
		_tabId = tabId;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(_tabId); // tab id
		if (_tabId != 7)
		{
			writeD(0);
		}
		else
		{
			List<CollectionTemplate> list = CollectionsHolder.getInstance().getCollectionsByTabId(_tabId);
			int count = 0;
			Map<Integer, Integer> collections = new HashMap<>();
			for (CollectionTemplate template : list)
			{
				if (_player.getCollectionList().contains(template.getId()))
				{
					count++;
					collections.put(count, template.getId());
				}
			}
			writeD(count);
			for (int id : collections.keySet())
			{
				writeH(collections.get(id)); // collection id
				writeD(0); // expiration time in seconds
			}
		}
	}
}