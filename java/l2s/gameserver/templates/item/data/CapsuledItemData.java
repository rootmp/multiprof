package l2s.gameserver.templates.item.data;

public class CapsuledItemData extends RewardItemData
{
	private final int _enchantLevel;
	private final boolean _announce;

	public CapsuledItemData(int id, long minCount, long maxCount, double chance, int enchantLevel, boolean announce)
	{
		super(id, minCount, maxCount, chance);
		_enchantLevel = enchantLevel;
		_announce = announce;
	}

	public int getEnchantLevel()
	{
		return _enchantLevel;
	}

	public boolean isAnnounce()
	{
		return _announce;
	}
}
