package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.PledgeContributionHolder;
import l2s.gameserver.templates.ClanContribution;

public class PledgeContributionParser extends AbstractParser<PledgeContributionHolder>
{
	private static PledgeContributionParser _instance = new PledgeContributionParser();

	public static PledgeContributionParser getInstance()
	{
		return _instance;
	}

	public PledgeContributionParser()
	{
		super(PledgeContributionHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/pledge_contribution.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "pledge_contribution.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();

			if("donations_available".equals(element.getName()))
				getHolder().setDonationsAvailable(Integer.parseInt(element.attributeValue("count", "3")));

			if("contribution".equals(element.getName()))
			{
				ClanContribution _contribution = new ClanContribution();
				for(Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
				{
					Element subElement = subIterator.next();
					if("ingredient".equals(subElement.getName()))
					{
						_contribution._ingredientItemId = Integer.parseInt(subElement.attributeValue("id"));
						_contribution._ingredientCount = Integer.parseInt(subElement.attributeValue("count"));
					}
					if("clan_exp".equals(subElement.getName()))
						_contribution._expCount = Integer.parseInt(subElement.attributeValue("count"));

					if("reward".equals(subElement.getName()))
					{
						_contribution._rewardChance = Integer.parseInt(subElement.attributeValue("chance", "0"));
						_contribution._rewardItemId = Integer.parseInt(subElement.attributeValue("id", "0"));
						_contribution._rewardCount = Integer.parseInt(subElement.attributeValue("count", "0"));
					}
				}
				getHolder().addContribution(Integer.parseInt(element.attributeValue("id")), _contribution);
			}
		}
	}
}