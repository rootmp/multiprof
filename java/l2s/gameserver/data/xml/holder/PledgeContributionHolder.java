package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.ClanContribution;

public final class PledgeContributionHolder extends AbstractHolder
{
	private static PledgeContributionHolder _instance = new PledgeContributionHolder();

	private int donationsAvailable = 3;
	private final Map<Integer, ClanContribution> _contributions = new HashMap<Integer, ClanContribution>();
	
	public static PledgeContributionHolder getInstance()
	{
		return _instance;
	}

	private PledgeContributionHolder()
	{}

	public void addContribution(int key, ClanContribution value)
	{
		_contributions.put(key, value);
	}

	public int getDonationsAvailable()
	{
		return donationsAvailable;
	}
	
	@Override
	public int size()
	{
		return _contributions.size();
	}

	@Override
	public void clear()
	{
		_contributions.clear();
	}

	public void setDonationsAvailable(int value)
	{
		donationsAvailable = value;
		
	}

	public ClanContribution getContribution(int _donateType)
	{
		return _contributions.get(_donateType);
	}
}
