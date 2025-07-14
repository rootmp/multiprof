package l2s.dataparser.data.holder.upgrade_system;

import java.util.List;

import l2s.dataparser.data.annotations.array.DoubleArray;
import l2s.dataparser.data.annotations.array.ObjectArray;
import l2s.dataparser.data.annotations.value.EnumValue;
import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.annotations.value.LongValue;
import l2s.dataparser.data.annotations.value.ObjectValue;

public class NormalUpgradeData
{
	@IntValue
	public int upgrade_id;
	@EnumValue
	public UpgradeType type;
	@ObjectValue
	public UpgradeItem upgrade_item;
	@ObjectArray(canBeNull = false)
	public List<UpgradeItemData> material_items;
	@LongValue
	public long commission;
	@ObjectArray(canBeNull = false)
	public List<UpgradeItemData> success_result_items;
	@ObjectArray(canBeNull = false)
	public List<UpgradeItemData> fail_result_items;
	@ObjectArray(canBeNull = false)
	public List<UpgradeItemData> bonus_items;
	@DoubleArray
	public double[] probability;
	
	public UpgradeType getType()
	{
		return type;  
	}

	public long getPrice()
	{
		return commission;
	}

	public int getItemId()
	{
		return upgrade_item.id;    
	}
	
	public int getEnchantLevel()
	{
		return upgrade_item.enchant;    
	}

	public double getSuccessProb()
	{
		return probability[0];
	}
	
	public double getFailProb()
	{
		return probability[1];    
	}
	
	public double getBonusProb()
	{
		return probability[2];
	}
	
	public List<UpgradeItemData> getBonusResult()
	{
		return bonus_items; 
	}

	public List<UpgradeItemData> getRequiredItems()
	{
		return material_items;   
	}
}
