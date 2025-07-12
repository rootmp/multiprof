package l2s.dataparser.data.common;

import l2s.dataparser.data.annotations.class_annotations.ParseSuper;
import l2s.dataparser.data.annotations.value.LinkedValue;
import l2s.dataparser.data.annotations.value.LongValue;

@ParseSuper
public class ItemRequired
{
	@LinkedValue(withoutName = true, LinkedType = LinkedType.linked_item)
	public int itemName; // Название предмета
	@LongValue(withoutName = true)
	public long count; // Количество
}