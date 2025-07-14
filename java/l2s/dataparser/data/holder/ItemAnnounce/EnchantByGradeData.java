package l2s.dataparser.data.holder.ItemAnnounce;

import l2s.dataparser.data.annotations.value.EnumValue;
import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.holder.itemdata.ItemData.ItemType;
import l2s.gameserver.templates.item.ItemGrade;

public class EnchantByGradeData
{
	@EnumValue(customRegex = "\\s*?=\\s*?([\\S*]+)", replaceChars = {
			'[', ']'
	})
	public ItemGrade item_grade;
	@EnumValue(customRegexNoCache = "\\s*?=\\s*?([\\S*]+)", replaceChars = {
			'[', ']'
	})
	public ItemType item_type;
	@IntValue
	public int announce_level;
}
