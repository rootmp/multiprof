package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.RandomCraftListHolder;
import l2s.gameserver.templates.randomCraft.RandomCraftRewardData;

public class RandomCraftListParser extends AbstractParser<RandomCraftListHolder>
{
	private static RandomCraftListParser _instance = new RandomCraftListParser();

	public static RandomCraftListParser getInstance()
	{
		return _instance;
	}

	private RandomCraftListParser()
	{
		super(RandomCraftListHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/RandomCraftRewardData.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "RandomCraftRewardData.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator("rewards"); iterator.hasNext();)
		{
			Element rewardsElement = iterator.next();
			int slot = parseInt(rewardsElement, "slot");
			long prob = 0;
			List<RandomCraftRewardData> data = new ArrayList<>();
			for(Iterator<Element> itemIterator = rewardsElement.elementIterator("item"); itemIterator.hasNext();)
			{
				Element itemElement = itemIterator.next();

				int id = parseInt(itemElement, "id");
				long count = parseLong(itemElement, "count", 1);
				double chance = parseDouble(itemElement, "chance", 0);
				boolean announce = parseBoolean(itemElement, "announce", false);
				int stage = parseInt(itemElement, "stage", -1);
				if(announce)//убрать
					stage = 1;
				long internalChance = BigDecimal.valueOf(chance).multiply(BigDecimal.valueOf(1_000_000)).setScale(0, RoundingMode.HALF_UP).longValue();
				prob += internalChance;

				data.add(new RandomCraftRewardData(id, count, internalChance, stage));
				if(announce)
					getHolder().addAnnounce(id);
			}
			if(slot == -1)
			{
				for(int i = 0; i < 5; i++)
					getHolder().addRandomCraftInfo(i, data, prob);
			}
			else
				getHolder().addRandomCraftInfo(slot, data, prob);
		}
	}

}
