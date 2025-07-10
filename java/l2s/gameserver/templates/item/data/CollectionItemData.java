package l2s.gameserver.templates.item.data;

/**
 * @author nexvill
 */
public class CollectionItemData extends ItemData
{
	private final int _enchantLevel;
	private final int _alternativeId;
	private final int _slotId;

	public CollectionItemData(int id, long count, int enchantLevel, int alternativeId, int slotId)
	{
		super(id, count);
		_enchantLevel = enchantLevel;
		_alternativeId = alternativeId;
		_slotId = slotId;
	}

	public int getEnchantLevel()
	{
		return _enchantLevel;
	}

	public int getAlternativeId()
	{
		return _alternativeId;
	}

	public int getSlotId()
	{
		return _slotId;
	}
}
