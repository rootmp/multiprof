package l2s.gameserver.templates.relics;

import java.util.List;

public class RelicsSummonInfo
{
	private final int summonId;
	private final int count;
	private final int remainTime;
	private final int itemId;
	private final int price;
	private final int daily_limit;
	private final List<RelicsProb> relicProbs;


	public RelicsSummonInfo(int summonId,int count, int itemId, int price, int remainTime, int daily_limit, List<RelicsProb> relicProbs)
	{
		this.summonId = summonId;
		this.count = count;
		this.remainTime = remainTime;
		this.itemId = itemId;
		this.price = price;
		this.daily_limit = daily_limit;
		this.relicProbs = relicProbs;
	}

	public int getSummonId()
	{
		return summonId;
	}

	public int getRemainTime()
	{
		return remainTime;
	}

	public int getItemId()
	{
		return itemId;
	}

	public int getPrice()
	{
		return price;
	}

	public int getCount()
	{
		return count;
	}

	public List<RelicsProb> getRelicProbs()
	{
		return relicProbs;
	}

	public int getDailyLimit()
	{
		return daily_limit;
	}
}
