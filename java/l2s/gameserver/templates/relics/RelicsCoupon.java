package l2s.gameserver.templates.relics;

import java.util.ArrayList;
import java.util.List;

public class RelicsCoupon
{
	private int id;
	private int count;
	private List<RelicsProb> relics;

	public RelicsCoupon(int id, int count)
	{
		this.id = id;
		this.count = count;
		this.relics = new ArrayList<>();
	}

	public int getId()
	{
		return id;
	}

	public int getCount()
	{
		return count;
	}

	public List<RelicsProb> getRelicsProb()
	{
		return relics;
	}

	public void addRelicProb(RelicsProb prob)
	{
		relics.add(prob);
	}
}
