package l2s.gameserver.network.l2.s2c.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.clientDat.CollectionsData;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.CollectionTemplate;

/**
 * @author nexvill
 * packet used show only active event collections, that player have
 * otherwise just send tabId and count = 0
 */
public class ExCollectionList implements IClientOutgoingPacket
{
	private Player _player;
	private int _tabId;

	public ExCollectionList(Player player, int tabId)
	{
		_player = player;
		_tabId = tabId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_tabId); // tab id
		if(_tabId != 7)
		{
			packetWriter.writeD(0);
		}
		else
		{
			List<CollectionTemplate> list = CollectionsData.getInstance().getCollectionsByTabId(_tabId);
			int count = 0;
			Map<Integer, Integer> collections = new HashMap<>();
			for(CollectionTemplate template : list)
			{
				if(_player.getCollectionList().contains(template.getId()))
				{
					count++;
					collections.put(count, template.getId());
				}
			}
			packetWriter.writeD(count);
			for(int id : collections.keySet())
			{
				packetWriter.writeH(collections.get(id)); //nCollectionID
				packetWriter.writeD(0); //nRemainTime expiration time in seconds
			}
			packetWriter.writeD(0); //nRemainTime
		}
		return true;
	}
}