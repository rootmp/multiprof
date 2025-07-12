package l2s.dataparser.data.holder;

import java.util.List;
import l2s.commons.data.xml.AbstractHolder;
import l2s.dataparser.data.annotations.Element;
import l2s.dataparser.data.holder.synthesis.SynthesisData;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

public class SynthesisHolder extends AbstractHolder
{
	@Element(start = "combinationitem_begin", end = "combinationitem_end")
	private static List<SynthesisData> synthesisData;
	private static SynthesisHolder ourInstance = new SynthesisHolder();

	public static SynthesisHolder getInstance()
	{
		return ourInstance;
	}

	@Override
	public void afterParsing() 
	{
		if (synthesisData == null)
			return;

		for (SynthesisData data : synthesisData)
		{
			if (data.successItem != null)
			{
				double rawChance = data.successItem.getChance();
				double percentChance = rawChance / 10000.0;
				data.successItem.setChance(percentChance);
			}

			if (data.failItem != null)
			{
				double rawChance = data.failItem.getChance();
				double percentChance = rawChance / 10000.0;
				data.failItem.setChance(percentChance);
			}
		}
		super.afterParsing();
	}


	public SynthesisData[] getDatas()
	{
		return synthesisData.toArray(new SynthesisData[synthesisData.size()]);
	}

	public SynthesisData getSynthesisData(Player player, int nOneSlotServerID, int nTwoSlotServerID)
	{
		final ItemInstance item1 = player.getInventory().getItemByObjectId(nOneSlotServerID);
		if(item1 == null)
			return null;
		
		final ItemInstance item2 = player.getInventory().getItemByObjectId(nTwoSlotServerID);
		if(item2 == null)
			return null;
		
		SynthesisData data = null;
		for(SynthesisData d : getDatas())
		{
			if(item1 == null || item1.getItemId() == d.getItem1Id())
			{
				if(item2.getItemId() == d.getItem2Id())
				{
					data = d;
					break;
				}
			}

			if(item1 == null || item1.getItemId() == d.getItem2Id())
			{
				if(item2.getItemId() == d.getItem1Id())
				{
					data = d;
					break;
				}
			}
		}
		
		return data; 
	}
	
	@Override
	public int size()
	{
		return synthesisData.size();
	}

	@Override
	public void clear()
	{
		synthesisData.clear();
	}
}