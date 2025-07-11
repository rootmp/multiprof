package l2s.gameserver.templates.item.henna;

import java.util.Map;

import l2s.gameserver.templates.item.data.ItemData;


/**
 * @author Serenitty
 */
public class DyePotentialFee
{
	private final int _step;
	private final ItemData _item;
	private final int _dailyCount;
	private final Map<Integer, Double> _enchantExp;
	private ItemData _altItem;
	
	public DyePotentialFee(int step, ItemData item, ItemData altItem, int dailyCount, Map<Integer, Double> enchantExp)
	{
		_step = step;
		_item = item;
		_dailyCount = dailyCount;
		_enchantExp = enchantExp;
		_altItem = altItem;
	}
	
	public int getStep()
	{
		return _step;
	}
	
	public ItemData getItem(int _costItemID)
	{
		if(_costItemID ==_item.getId())
			return _item;
		if(_costItemID == _altItem.getId())
			return _altItem;
		return null;
	}
	
	public int getDailyCount()
	{
		return _dailyCount;
	}
	
	public Map<Integer, Double> getEnchantExp()
	{
		return _enchantExp;
	}
}