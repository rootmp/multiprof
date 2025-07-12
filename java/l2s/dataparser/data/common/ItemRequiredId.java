package l2s.dataparser.data.common;

import l2s.dataparser.data.annotations.class_annotations.ParseSuper;
import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.annotations.value.LongValue;

@ParseSuper
public class ItemRequiredId
{
	@IntValue(withoutName = true)
	public int itemName; // Название предмета
	@LongValue(withoutName = true)
	public long count; // Количество
}