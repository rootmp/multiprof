package l2s.dataparser.data.holder.ItemAnnounce;

import l2s.dataparser.data.annotations.array.IntArray;
import l2s.dataparser.data.annotations.array.ObjectArray;
import l2s.dataparser.data.common.ItemRequiredId;

public class UserCraftData 
{
	@IntArray
	public int[] item_list;
	@ObjectArray
	public ItemRequiredId[] enchanted_item_list;
}
