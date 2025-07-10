package l2s.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.henna.DyeCombintation;
import l2s.gameserver.templates.henna.HennaTemplate;
import l2s.gameserver.templates.henna.PotentialEffect;
import l2s.gameserver.templates.henna.PotentialFee;

public final class HennaHolder extends AbstractHolder
{
	private static final HennaHolder INSTANCE = new HennaHolder();

	public static HennaHolder getInstance()
	{
		return INSTANCE;
	}

	private final Map<Integer, PotentialEffect> potentialEffects = new HashMap<>();
	private final Map<Integer, Integer> potentialExp = new HashMap<>();
	private final Map<Integer, PotentialFee> potentialFees = new HashMap<>();
	private final List<DyeCombintation> combinations = new ArrayList<>();
	private final Map<Integer, HennaTemplate> hennas = new HashMap<>();
	private final Map<Integer, HennaTemplate> hennasByItemId = new HashMap<>();

	public void addPotentialEffect(PotentialEffect effect)
	{
		potentialEffects.put(effect.getId(), effect);
	}

	public PotentialEffect getPotentialEffect(int id)
	{
		return potentialEffects.get(id);
	}

	public List<PotentialEffect> getPotentialEffects(int slot)
	{
		return potentialEffects.values().stream().filter(p -> p.getSlotId() == slot).collect(Collectors.toList());
	}

	public void addPotentialExp(int level, int exp)
	{
		potentialExp.put(level, exp);
	}

	public int getEnchantStep(int exp)
	{
		int step = 1;
		Integer enchantExp;
		while ((enchantExp = potentialExp.get(step)) != null)
		{
			exp -= enchantExp;
			if (exp >= 0)
			{
				step++;
				continue;
			}
			break;
		}
		return step;
	}

	public int getEnchantExp(int step, int exp)
	{
		for (int i = 1; i <= step; i++)
		{
			Integer enchantExp = potentialExp.get(i);
			if (enchantExp != null)
			{
				if (exp >= enchantExp)
				{
					exp -= enchantExp;
					continue;
				}
			}
			break;
		}
		return exp;
	}

	public void addPotentialFee(PotentialFee fee)
	{
		potentialFees.put(fee.getStep(), fee);
	}

	public PotentialFee getPotentialFee(int step)
	{
		return potentialFees.get(step);
	}

	public void addCombination(DyeCombintation combination)
	{
		combinations.add(combination);
	}

	public List<DyeCombintation> getCombinations()
	{
		return combinations;
	}

	public void addHenna(HennaTemplate h)
	{
		hennas.put(h.getSymbolId(), h);
		hennasByItemId.put(h.getDyeId(), h);
	}

	public HennaTemplate getHenna(int symbolId)
	{
		return hennas.get(symbolId);
	}

	public HennaTemplate getHennaByItemId(int itemId)
	{
		return hennasByItemId.get(itemId);
	}

	public Collection<HennaTemplate> getHennas()
	{
		return hennas.values();
	}

	@Override
	public int size()
	{
		return hennas.size();
	}

	@Override
	public void clear()
	{
		potentialEffects.clear();
		hennas.clear();
		hennasByItemId.clear();
	}
}
