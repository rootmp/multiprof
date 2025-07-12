package l2s.dataparser.data.holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.dataparser.data.annotations.Element;
import l2s.dataparser.data.holder.dyedata.DyeData;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.henna.Henna;

/**
 * @author : Camelion
 * @date : 27.08.12 1:36
 */
public class DyeDataHolder extends AbstractHolder
{
	private static DyeDataHolder ourInstance = new DyeDataHolder();
	
	@Element(start = "dye_begin", end = "dye_end")
	private List<DyeData> dyes;

	private final Map<Integer, Henna> _hennaDyeIdList = new HashMap<>();
	private final Map<Integer, Henna> _hennaItemIdList = new HashMap<>();
	
	@Override
	public void afterParsing() 
	{
		for (DyeData dye : dyes) 
		{
			_hennaDyeIdList.put(dye.dye_id, new Henna(dye));
			_hennaItemIdList.put(dye.dye_item_id, new Henna(dye));
		}
	}
	
	public Henna getHennaByDyeId(int id)
	{
		return _hennaDyeIdList.get(id);
	}
	
	public Henna getHennaByItemId(int id)
	{
		return _hennaItemIdList.get(id);
	}
	
	public static DyeDataHolder getInstance()
	{
		return ourInstance;
	}

	private DyeDataHolder()
	{}

	@Override
	public int size()
	{
		return dyes.size();
	}

	public List<DyeData> getDyes()
	{
		return dyes;
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
	}

	/**
	 * Gets the henna list.
	 * @param player the player's class Id.
	 * @return the list with all the allowed dyes.
	 */
	public List<Henna> getHennaList(Player player)
	{
		final List<Henna> list = new ArrayList<>();
		for (Henna henna : _hennaDyeIdList.values())
		{
			if (player.getInventory().getItemByItemId(henna.getDyeItemId()) != null && henna.isAllowedClass(player))
				list.add(henna);
		}
		return list;
	}
}