package l2s.gameserver.data.xml.parser;

import java.io.File;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.AdditionalDropHolder;
import l2s.gameserver.model.reward.RewardData;
import l2s.gameserver.templates.npc.AdditionalDrop;

public final class AdditionalDropParser extends AbstractParser<AdditionalDropHolder>
{
	private AdditionalDropParser()
	{
		super(AdditionalDropHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/additional_drop/additional_drop.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "additional_drop.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Element element : rootElement.elements("drop"))
		{
			int minLevel = parseInt(element, "min_level", 1);
			int maxLevel = parseInt(element, "max_level", Integer.MAX_VALUE);
			boolean levelPenalty = parseBoolean(element, "level_penalty", true);
			int monsterType = parseInt(element, "monster_type", 0);
			AdditionalDrop additionalDrop = new AdditionalDrop(minLevel, maxLevel, levelPenalty, monsterType);
			for (Element element1 : element.elements("npcs"))
			{
				for (Element element2 : element1.elements("npc"))
				{
					additionalDrop.getNpcs().add(parseInt(element2, "id"));
				}
			}
			for (Element element1 : element.elements("reward"))
			{
				additionalDrop.getRewardItems().add(RewardData.parseReward(element1));
			}
			getHolder().addDrop(additionalDrop);
		}
	}
	
	public static AdditionalDropParser getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected final static AdditionalDropParser _instance = new AdditionalDropParser();
	}
}