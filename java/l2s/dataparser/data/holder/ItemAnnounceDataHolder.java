package l2s.dataparser.data.holder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import l2s.commons.data.xml.AbstractHolder;
import l2s.dataparser.data.annotations.Element;
import l2s.dataparser.data.holder.ItemAnnounce.EnchantByGradeData;
import l2s.dataparser.data.holder.ItemAnnounce.EnchantByIdData;
import l2s.dataparser.data.holder.ItemAnnounce.ItemAnnounceData;
import l2s.dataparser.data.holder.ItemAnnounce.PurchaseLimitShopData;
import l2s.dataparser.data.holder.ItemAnnounce.RandomBoxData;
import l2s.dataparser.data.holder.ItemAnnounce.RelicsData;
import l2s.dataparser.data.holder.ItemAnnounce.UserCraftData;
import l2s.dataparser.data.holder.itemdata.ItemData.ItemType;
import l2s.gameserver.model.items.ItemInstance;

public class ItemAnnounceDataHolder extends AbstractHolder
{
	@Element(start = "user_craft_begin", end = "user_craft_end")
	public UserCraftData _userCraftData;

	@Element(start = "item_begin", end = "item_end")
	public List<ItemAnnounceData> _itemAnnounceData;

	@Element(start = "random_box_begin", end = "random_box_end")
	public List<RandomBoxData> _randomBoxData;

	@Element(start = "purchase_limit_shop_begin", end = "purchase_limit_shop_end")
	public PurchaseLimitShopData _purchaseLimitShopData;

	@Element(start = "relics_begin", end = "relics_end")
	public RelicsData _relicsData;
	
	@Element(start = "enchant_by_grade_begin", end = "enchant_by_grade_end")
	public List<EnchantByGradeData> _enchantByGradeData;

	@Element(start = "enchant_by_id_begin", end = "enchant_by_id_end")
	public List<EnchantByIdData> _enchantByIdData;

	private Map<Integer, Integer> _itemAnnounceDataMap;
	private Map<Integer, Integer> _itemEnchantByidDataMap;
	private List<Integer> _purchaseLimitShop;
	private List<Integer> _relics;
	
	private static ItemAnnounceDataHolder ourInstance = new ItemAnnounceDataHolder();

	public static ItemAnnounceDataHolder getInstance()
	{
		return ourInstance;
	}

	private ItemAnnounceDataHolder()
	{}

	@Override
	public void afterParsing()
	{
		super.afterParsing();
		_itemAnnounceDataMap = _itemAnnounceData.stream().collect(Collectors.toMap(item -> item.item_id, item -> item.announce_level));
		_itemEnchantByidDataMap = _enchantByIdData.stream().collect(Collectors.toMap(item -> item.item_id, item -> item.announce_level));
		_purchaseLimitShop = Arrays.stream(_purchaseLimitShopData.item_list).boxed().collect(Collectors.toList());
		_relics = Arrays.stream(_relicsData.relics_list).boxed().collect(Collectors.toList());
	}

	@Override
	public int size()
	{
		return _itemAnnounceData.size() + _randomBoxData.size() + _enchantByGradeData.size() + _enchantByIdData.size();
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
	}

	public int findEnchantItemAnnounceLevel(ItemInstance item)
	{
		if(item == null)
			return 0;
		int announceLevel = _itemEnchantByidDataMap.getOrDefault(item.getItemId(), 0);
		if(announceLevel > 0)
			return announceLevel;

		ItemType itemType = null;
		if(item.isAccessory())
			itemType = ItemType.accessary;
		else if(item.isArmor())
			itemType = ItemType.armor;
		else if(item.isWeapon())
			itemType = ItemType.weapon;

		if(itemType != null)
		{
			for(EnchantByGradeData data : _enchantByGradeData)
			{
				if(data.item_grade == item.getGrade() && data.item_type == itemType)
					return data.announce_level;
			}
		}
		return 0;
	}

	public Integer getItemAnnounce(int item_id)
	{
		return _itemAnnounceDataMap.getOrDefault(item_id, 0);
	}

	public List<Integer> getPurchaseLimitShop()
	{
		return _purchaseLimitShop;
	}

	public List<Integer> getRelics()
	{
		return _relics;
	}
}