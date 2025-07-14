package l2s.dataparser.data.holder.dyedata;

import l2s.dataparser.data.annotations.array.IntArray;
import l2s.dataparser.data.annotations.array.ObjectArray;
import l2s.dataparser.data.annotations.array.StringArray;
import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.annotations.value.StringValue;
import l2s.dataparser.data.common.ItemRequiredId;

public class DyeData
{
	@StringValue
	public String dye_name; // Название, есть в itemdata.txt
	@IntValue
	public int dye_id; // ID
	@IntValue
	public int dye_item_id; // Item ID
	@IntValue
	public int dye_level; //
	@IntValue
	public int str; // STR
	@IntValue
	public int con; // CON
	@IntValue
	public int dex; // DEX
	@IntValue(name = "int")
	public int _int; // INT
	@IntValue
	public int men; // MEN
	@IntValue
	public int wit; // WIT
	@IntValue
	public int cha; // cha
	@IntValue
	public int luc; // luc
	@IntValue
	public int need_count; // Необходимое кол-во таких предметов для нанесения тату
	@ObjectArray
	public ItemRequiredId[] wear_fee;
	@IntValue
	public int cancel_count; // Количество предметов, возвращаемое при снятии тату
	@ObjectArray
	public ItemRequiredId[] cancel_fee; // Цена снятия тату
	@IntArray
	public int[] wear_class; // Список классов, которым доступна эта тату
	@StringArray
	public String[] dye_skill;
	@StringArray
	public String[] dye_unequip_skill;
	@StringValue
	public String dye_cond;
	@IntValue
	public int dye_premium;
	@IntValue
	public int dye_period;
}
