package l2s.gameserver.templates.randomCraft;

/**
 * @author Mode
 */
public class RandomCraftRewardItem
{
	private final int _id;
	private final long _count;
	private boolean _locked;
	private int _lockLeft;
	private int _enchant;

	public RandomCraftRewardItem(int id, long count, int enchant, boolean locked, int lockLeft)
	{
		_id = id;
		_count = count;
		_enchant = enchant;
		_locked = locked;
		_lockLeft = lockLeft;
	}

	public int getItemId()
	{
		return _id;
	}

	public long getItemCount()
	{
		return _count;
	}

	public boolean isLocked()
	{
		return _locked;
	}

	public int getLockLeft()
	{
		return _lockLeft;
	}

	public void lock()
	{
		_locked = true;
	}

	public void decLock()
	{
		_lockLeft--;
		if(_lockLeft <= 0)
		{
			_locked = false;
		}
	}

	public int getEnchant()
	{
		return _enchant;
	}
}
