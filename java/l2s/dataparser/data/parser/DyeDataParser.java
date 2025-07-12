package l2s.dataparser.data.parser;

import l2s.dataparser.data.common.AbstractDataParser;
import l2s.dataparser.data.holder.DyeDataHolder;

/**
 * @author : Camelion
 * @date : 27.08.12 1:36
 */
public class DyeDataParser extends AbstractDataParser<DyeDataHolder>
{
	private static DyeDataParser ourInstance = new DyeDataParser();

	public static DyeDataParser getInstance()
	{
		return ourInstance;
	}

	private DyeDataParser()
	{
		super(DyeDataHolder.getInstance());
	}

	@Override
	protected String getFileName()
	{
		return "data/pts_scripts/Dyedata.txt";
	}
}