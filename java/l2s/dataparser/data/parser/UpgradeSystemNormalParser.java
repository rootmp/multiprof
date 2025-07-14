package l2s.dataparser.data.parser;

import l2s.dataparser.data.common.AbstractDataParser;
import l2s.dataparser.data.holder.UpgradeSystemNormalHolder;


public class UpgradeSystemNormalParser extends AbstractDataParser<UpgradeSystemNormalHolder>
{
	private static UpgradeSystemNormalParser ourInstance = new UpgradeSystemNormalParser();

	public static UpgradeSystemNormalParser getInstance()
	{
		return ourInstance;
	}

	private UpgradeSystemNormalParser()
	{
		super(UpgradeSystemNormalHolder.getInstance());
	}

	@Override
	protected String getFileName()
	{
		return "data/pts_scripts/upgradesystem_normal.txt";
	}
}