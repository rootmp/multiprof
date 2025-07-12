package l2s.dataparser.data.parser;

import l2s.dataparser.data.common.AbstractDataParser;
import l2s.dataparser.data.holder.SynthesisHolder;

public class SynthesisParser extends AbstractDataParser<SynthesisHolder>
{
	private static SynthesisParser ourInstance = new SynthesisParser();

	public static SynthesisParser getInstance()
	{
		return ourInstance;
	}

	private SynthesisParser()
	{
		super(SynthesisHolder.getInstance());
	}

	@Override
	protected String getFileName()
	{
		return "data/pts_scripts/synthesis/";
	}
}