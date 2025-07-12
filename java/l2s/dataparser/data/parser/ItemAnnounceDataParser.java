package l2s.dataparser.data.parser;

import l2s.dataparser.data.common.AbstractDataParser;
import l2s.dataparser.data.holder.ItemAnnounceDataHolder;


public class ItemAnnounceDataParser extends AbstractDataParser<ItemAnnounceDataHolder>
{
	private static ItemAnnounceDataParser ourInstance = new ItemAnnounceDataParser();

	public static ItemAnnounceDataParser getInstance()
	{
		return ourInstance;
	}

	private ItemAnnounceDataParser()
	{
		super(ItemAnnounceDataHolder.getInstance());
	}

	@Override
	protected String getFileName()
	{
		return "data/pts_scripts/ItemAnnounce.txt";
	}
}