package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.AdenLabDataHolder;
import l2s.gameserver.templates.adenLab.AdenLabStageTemplate;
import l2s.gameserver.templates.adenLab.CardselectOptionData;

public class AdenLabDataParser extends AbstractParser<AdenLabDataHolder>
{
	private static final AdenLabDataParser INSTANCE = new AdenLabDataParser();

	public static AdenLabDataParser getInstance()
	{
		return INSTANCE;
	}

	private AdenLabDataParser()
	{
		super(AdenLabDataHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/adenlab/AdenLabData.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "AdenLabData.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		Element config = rootElement.element("config");
		if(config != null)
		{
			Config.ADENLAB_RESEARCH_DIARY = parseIntArr(config, "research_diary", ",");
			Config.ADENLAB_ADENA_PLAY = parseLong(config, "adena_play");
			Config.ADENLAB_ADENA_FIX = parseLong(config, "adena_fix");
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("stage"); iterator.hasNext();)
		{
			Element stageElement = iterator.next();

			int id = parseInt(stageElement, "id");
			int cardCount = parseInt(stageElement, "card_count", 0);

			AdenLabStageTemplate stageTemplate = new AdenLabStageTemplate(id, cardCount);

			Element effectElement = stageElement.element("normal_effect");
			if(effectElement != null)
				stageTemplate.setNormalEffect(parseInt(effectElement, "id"));

			for(Iterator<Element> specialEffectIterator = stageElement.elementIterator("special_effect"); specialEffectIterator.hasNext();)
			{
				Element specialEffectElement = specialEffectIterator.next();

				int slotIndex = parseInt(specialEffectElement, "slot", -1);

				if(slotIndex < 0 || slotIndex > 1)
				{
					_log.warn("Invalid slot index " + slotIndex + " in stage " + id + ", skipping...");
					continue;
				}
				stageTemplate.specialOptionsId[slotIndex] = parseInt(specialEffectElement, "option", 0);

				for(Iterator<Element> optionIterator = specialEffectElement.elementIterator("option"); optionIterator.hasNext();)
				{
					Element optionElement = optionIterator.next();
					int level = parseInt(optionElement, "level", 1);
					double chance = parseDouble(optionElement, "chance", 0.0);
					stageTemplate.addSpecialOption(slotIndex, new CardselectOptionData(level, chance));
				}

			}

			stageTemplate.generateSpecialGradeProbs();

			getHolder().addStage(stageTemplate);
		}
	}

}
