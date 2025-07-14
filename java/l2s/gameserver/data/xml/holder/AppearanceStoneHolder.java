package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.Map;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.Visual;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.item.support.AppearanceStone;

/**
 * @author Bonux
**/
public class AppearanceStoneHolder extends AbstractHolder
{
	private static final AppearanceStoneHolder _instance = new AppearanceStoneHolder();

	private static Map<Integer, ItemData> ITEMS_DRESSING_ROOM = new HashMap<Integer, ItemData>();
	private TIntObjectMap<AppearanceStone> _stones = new TIntObjectHashMap<AppearanceStone>();

	public static AppearanceStoneHolder getInstance()
	{
		return _instance;
	}

	public void addAppearanceStone(AppearanceStone stone)
	{
		_stones.put(stone.getItemId(), stone);
	}

	public AppearanceStone getAppearanceStone(int id)
	{
		return _stones.get(id);
	}

	public AppearanceStone getStoneId(Player player, int visualId)
	{
		for(AppearanceStone stone : _stones.valueCollection())
		{
			for(Visual visual : stone.getVisual().values())
			{
				if(visual.getExtractId() == visualId)
					return stone;
				if(visual.getAlternative().containsKey(player.getRace()) && visual.getAlternative().get(player.getRace()) == visualId)
					return stone;
			}
		}
		return null;
	}

	@Override
	public int size()
	{
		return _stones.size();
	}

	@Override
	public void clear()
	{
		_stones.clear();
	}

	public void addItemDreassing(int id, int price_id, long price_count)
	{
		ITEMS_DRESSING_ROOM.put(id, new ItemData(id, price_id, price_count, 0));
	}

	public Map<Integer, ItemData> getItemsDreasing()
	{
		return ITEMS_DRESSING_ROOM;
	}
}
