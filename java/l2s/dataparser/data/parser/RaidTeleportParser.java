package l2s.dataparser.data.parser;

import l2s.dataparser.data.common.AbstractDataParser;
import l2s.dataparser.data.holder.RaidTeleportHolder;

public class RaidTeleportParser extends AbstractDataParser<RaidTeleportHolder>
{
	private static RaidTeleportParser ourInstance = new RaidTeleportParser();

	public static RaidTeleportParser getInstance()
	{
		return ourInstance;
	}

	private RaidTeleportParser()
	{
		super(RaidTeleportHolder.getInstance());
	}

	@Override
	protected String getFileName()
	{
		return "data/pts_scripts/RaidTeleport.txt";
	}
}