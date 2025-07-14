package l2s.gameserver.data.xml.parser;

import java.io.File;
import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.GlobalEventUiHolder;

public final class GlobalEventUiParser extends AbstractParser<GlobalEventUiHolder>
{
	private static final GlobalEventUiParser INSTANCE = new GlobalEventUiParser();

	private GlobalEventUiParser()
	{
		super(GlobalEventUiHolder.getInstance());
	}

	public static GlobalEventUiParser getInstance()
	{
		return INSTANCE;
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/global_event_ui.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "global_event_ui.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Element element : rootElement.elements("event"))
		{
			int Id = parseInt(element, "id");
			int multisell = parseInt(element, "multisell");
			getHolder().addEvent(Id, multisell);
		}
	}
}
