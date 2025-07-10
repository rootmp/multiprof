package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.MissionLevelRewardsHolder;
import l2s.gameserver.templates.dailymissions.MissionLevelRewardTemplate;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.item.data.MissionLevelRewardData;

/**
 * @author nexvill
 */
public class MissionLevelRewardsParser extends AbstractParser<MissionLevelRewardsHolder>
{
	private static final MissionLevelRewardsParser INSTANCE = new MissionLevelRewardsParser();

	public static MissionLevelRewardsParser getInstance()
	{
		return INSTANCE;
	}

	private MissionLevelRewardsParser()
	{
		super(MissionLevelRewardsHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/essence/mission_level_rewards.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "mission_level_rewards.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator("date_info"); iterator.hasNext();)
		{
			Element firstElement = iterator.next();
			int month = parseInt(firstElement, "month");
			int year = parseInt(firstElement, "year");
			int maxRewardLvl = parseInt(firstElement, "max_reward_lvl");
			int finalRewardId = parseInt(firstElement, "final_reward_id");
			int finalRewardCount = parseInt(firstElement, "final_reward_count");
			ItemData finalReward = new ItemData(finalRewardId, finalRewardCount);
			MissionLevelRewardTemplate template = new MissionLevelRewardTemplate(month, year, maxRewardLvl, finalReward);

			for (Iterator<Element> secondIterator = firstElement.elementIterator("points"); secondIterator.hasNext();)
			{
				Element secondElement = secondIterator.next();
				int level = parseInt(secondElement, "level");
				int value = parseInt(secondElement, "value");
				int baseRewardId = parseInt(secondElement, "base_reward_id");
				int baseRewardCount = parseInt(secondElement, "base_reward_count");
				int additionalRewardId = parseInt(secondElement, "additional_reward_id", 0);
				int additionalRewardCount = parseInt(secondElement, "additional_reward_count", 0);

				template.addReward(new MissionLevelRewardData(level, value, baseRewardId, baseRewardCount, additionalRewardId, additionalRewardCount));
			}

			getHolder().addReward(template);
		}
	}
}
