package l2s.gameserver.templates.item.data;

public class CollectionItemData extends ItemData
{
	private final int _enchantLevel;
	private final int _slotId;
	private final int _BlessCondition;
	private int _bless;

	public CollectionItemData(int id, long count, int enchantLevel, int bless, int slotId, int BlessCondition)
	{
		super(id, count);
		_enchantLevel = enchantLevel;
		_bless = bless;
		_BlessCondition = BlessCondition;
		_slotId = slotId;
	}

	public int getEnchantLevel()
	{
		return _enchantLevel;
	}

	public int getSlotId()
	{
		return _slotId;
	}

	public int getBlessCondition()
	{
		return _BlessCondition;
	}

	public int getBless()
	{
		return _bless;
	}
}
