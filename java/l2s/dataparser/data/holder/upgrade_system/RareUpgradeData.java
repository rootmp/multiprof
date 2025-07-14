package l2s.dataparser.data.holder.upgrade_system;

import java.util.List;

import l2s.dataparser.data.annotations.array.IntArray;
import l2s.dataparser.data.annotations.array.ObjectArray;
import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.annotations.value.LongValue;
import l2s.dataparser.data.annotations.value.ObjectValue;

public class RareUpgradeData
{
	@IntValue
	public int upgrade_id;
	@ObjectValue
	public UpgradeItem upgrade_item;
	@ObjectArray(canBeNull = false)
	public List<UpgradeItemData> material_items;
	@LongValue
	public long commission;
	@IntArray
	public int[] result_item;
	
	public int getItemId()
	{
		return upgrade_item.id;   
	}
	
	public int getEnchantLevel()
	{
		return upgrade_item.enchant;   
	}

	public long getPrice()
	{
		return commission;  
	}
	
	public List<UpgradeItemData> getRequiredItems()
	{
		return material_items;   
	}

	public int getResultItemId()
	{
		return result_item[0];
	}

	public int getResultItemEnchant()
	{
		return result_item[1];
	}

	public int getResultItemCount()
	{
		return 1; 
	}
}
