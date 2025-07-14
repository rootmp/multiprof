package l2s.gameserver.data.xml.holder;

import java.util.Collection;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.PremiumAccountTemplate;

/**
 * @author Bonux
 **/
public class PremiumAccountHolder extends AbstractHolder
{
	private static final PremiumAccountHolder _instance = new PremiumAccountHolder();

	private final IntObjectMap<PremiumAccountTemplate> _premiumAccounts = new TreeIntObjectMap<PremiumAccountTemplate>();

	public static PremiumAccountHolder getInstance()
	{
		return _instance;
	}

	public void addPremiumAccount(PremiumAccountTemplate premiumAccount)
	{
		_premiumAccounts.put(premiumAccount.getType(), premiumAccount);
	}

	public PremiumAccountTemplate getPremiumAccount(int type)
	{
		if(type == 0 && !_premiumAccounts.containsKey(type))
			return PremiumAccountTemplate.DEFAULT_ACCOUNT_TEMPLATE;
		return _premiumAccounts.get(type);
	}

	public Collection<PremiumAccountTemplate> getPremiumAccounts()
	{
		return _premiumAccounts.valueCollection();
	}

	@Override
	public int size()
	{
		return _premiumAccounts.size();
	}

	@Override
	public void clear()
	{
		_premiumAccounts.clear();
	}
}
