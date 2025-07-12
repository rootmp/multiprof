package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.RelicsSynthesisHolder;
import l2s.gameserver.templates.relics.RelicsProb;

public final class RelicsSynthesisParser extends AbstractParser<RelicsSynthesisHolder> 
{
	private static final RelicsSynthesisParser _instance = new RelicsSynthesisParser();

	public static RelicsSynthesisParser getInstance() 
	{
		return _instance;
	}

	private RelicsSynthesisParser() 
	{
		super(RelicsSynthesisHolder.getInstance());
	}

	@Override
	public File getXMLPath() 
	{
		return new File(Config.DATAPACK_ROOT, "data/relics/relics_synthesis.xml");
	}

	@Override
	public String getDTDFileName() 
	{
		return "relics_synthesis.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception 
	{
		for (Iterator<Element> iterator = rootElement.elementIterator("grade"); iterator.hasNext();) 
		{
			Element cuponElement = iterator.next();
			int id = Integer.parseInt(cuponElement.attributeValue("id"));

			List<RelicsProb> _prob = new ArrayList<>();

			for (Iterator<Element> relicIterator = cuponElement.elementIterator("relic"); relicIterator.hasNext();) 
			{
				Element relicElement = relicIterator.next();
				int relicId = Integer.parseInt(relicElement.attributeValue("relics_id"));
				long chance = Long.parseLong(relicElement.attributeValue("prob", "0"));
				_prob.add(new RelicsProb(relicId, chance));
			}

			getHolder().addSynthesis(id, _prob);
		}
	}
}
