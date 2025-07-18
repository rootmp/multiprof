package l2s.gameserver.cache;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.items.ItemInfo;
import l2s.gameserver.model.items.ItemInstance;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class ItemInfoCache
{
	private Cache cache;

	private ItemInfoCache()
	{
		cache = CacheManager.getInstance().getCache(this.getClass().getName());
	}

	public void put(ItemInstance item)
	{
		cache.put(new Element(item.getObjectId(), new ItemInfo(item)));
	}

	public ItemInfo get(int objectId)
	{
		final Element element = cache.get(objectId);
		ItemInfo info = null;

		if(element != null)
			info = (ItemInfo) element.getObjectValue();

		Player player = null;

		if(info != null)
		{
			player = World.getPlayer(info.getOwnerId());

			ItemInstance item = null;

			if(player != null)
				item = player.getInventory().getItemByObjectId(objectId);

			if(item != null)
				if(item.getItemId() == info.getItemId())
					cache.put(new Element(item.getObjectId(), info = new ItemInfo(item)));
		}

		return info;
	}

	private final static ItemInfoCache _instance = new ItemInfoCache();

	public final static ItemInfoCache getInstance()
	{
		return _instance;
	}
}
