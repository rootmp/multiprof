package l2s.gameserver.data.xml.holder;

import java.util.Map;
import java.util.TreeMap;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.adenLab.AdenLabStageTemplate;


public final class AdenLabDataHolder extends AbstractHolder
{
	private static final AdenLabDataHolder INSTANCE = new AdenLabDataHolder();

	public static AdenLabDataHolder getInstance()
	{
		return INSTANCE;
	}

	private final Map<Integer, AdenLabStageTemplate> cardselectStage = new TreeMap<>();


	@Override
	public int size()
	{
		return cardselectStage.size();
	}

	@Override
	public void clear()
	{
		cardselectStage.clear();
	}

	public void addStage(AdenLabStageTemplate stageTemplate)
	{
		cardselectStage.put(stageTemplate.getId(), stageTemplate);  
	}

	public AdenLabStageTemplate getSlot(int nSlotID)
	{
		return cardselectStage.get(nSlotID);
	}
}
