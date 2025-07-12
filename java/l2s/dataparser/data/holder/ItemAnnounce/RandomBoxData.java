package l2s.dataparser.data.holder.ItemAnnounce;

import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.annotations.value.StringValue;

public class RandomBoxData
{
	@IntValue
	public int item_id;
	@IntValue
	public int announce_level;
	//@IntArray
	@StringValue
	public String fixed_items; //TODO всегда пустой?
}
