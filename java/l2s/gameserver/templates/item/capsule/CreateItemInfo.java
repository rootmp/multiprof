package l2s.gameserver.templates.item.capsule;

public class CreateItemInfo
{
	private final int nItemID;
	private final int nEnchant;
	private final long nItemCount;
	private final long nProb;

	public CreateItemInfo(int nItemID, int nEnchant, long nItemCount, long nProb)
	{
		this.nItemID = nItemID;
		this.nEnchant = nEnchant;
		this.nItemCount = nItemCount;
		this.nProb = nProb;
	}

	public int getItemId()
	{
		return nItemID;
	}

	public int getEnchant()
	{
		return nEnchant;
	}

	public long getItemCount()
	{
		return nItemCount;
	}

	public long getProb()
	{
		return nProb;
	}
}
