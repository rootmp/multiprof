package l2s.gameserver.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import l2s.commons.util.Rnd;

public enum ChallengeEffect
{
  BLANK(-1, -1, -1, -1, -1, -1, -1, null),
	ENCHANT_SUCCESS_PLUS_4_PERCENTS(0, 4, 0, 1, 7, 2, 15, Arrays.asList(1, 2, 3)),
	ENCHANT_SUCCESS_PLUS_8_PERCENTS(1, 8, 0, 1, 7, 2, 25, Arrays.asList(1, 2, 3)),
	ENCHANT_LEVEL_PLUS_ONE_TWO(2, 20, 80, 1, 6, 3, 50, Arrays.asList(2, 3)), // 80% +2 option
	ENCHANT_TO_ZERO(3, 0, 0, 1, 0, 3, 30, Collections.singletonList(1)),
	ENCHANT_MINUS_ONE_ON_FAIL(4, 100, 0, 1, 7, 3, 100, Arrays.asList(1, 2, 3)),
	ENCHANT_SAVE_ENCHANT_LEVEL(5, 30, 0, 1, 7, 3, 100, Arrays.asList(1, 2, 3));

	private final int optionIndex;
	private final int chance;
	private final int secondChange;
	private final int minEnchant;
	private final int maxEnchant;
	private final int groupOneMaxEnchant;
	private final int price;
	private final List<Integer> supportedGroups;

	public static ChallengeEffect getValueFromIndex(int id)
	{
		return Arrays.stream(ChallengeEffect.values()).filter(p -> p.getOptionIndex() == id).findFirst().orElse(null);
	}
	
	ChallengeEffect(int optionIndex, int chance, int secondChange, int minEnchant, int maxEnchant, int groupOneMaxEnchant, int price, List<Integer> supportedGroups)
	{
		this.optionIndex = optionIndex;
		this.chance = chance;
		this.secondChange = secondChange;
		this.minEnchant = minEnchant;
		this.maxEnchant = maxEnchant;
		this.groupOneMaxEnchant = groupOneMaxEnchant;
		this.price = price;
		this.supportedGroups = supportedGroups;
	}

	public int getOptionIndex()
	{
		return optionIndex;
	}

	public int getChance()
	{
		return chance;
	}

	public int getSecondChange()
	{
		return secondChange;
	}

	public int getMinEnchant()
	{
		return minEnchant;
	}

	public int getMaxEnchant()
	{
		return maxEnchant;
	}

	public int getGroupOneMaxEnchant()
	{
		return groupOneMaxEnchant;
	}

	public int getPrice()
	{
		return price;
	}

	public boolean isGroupSupported(int group)
	{
		if(isBlank())
			return false;

		return supportedGroups.contains(group);
	}

	public boolean isExtraEnchantChance()
	{
		if(isBlank())
			return false;

		return this.optionIndex == ENCHANT_SUCCESS_PLUS_4_PERCENTS.getOptionIndex() || this.optionIndex == ENCHANT_SUCCESS_PLUS_8_PERCENTS.getOptionIndex();
	}

	public boolean isEnchantLess()
	{
		if(isBlank())
			return false;

		return this.optionIndex == ENCHANT_TO_ZERO.getOptionIndex() || this.optionIndex == ENCHANT_MINUS_ONE_ON_FAIL.getOptionIndex();
	}

	public boolean isEnchantMinusOne()
	{
		if(isBlank())
			return false;

		return this.optionIndex == ENCHANT_MINUS_ONE_ON_FAIL.getOptionIndex();
	}

	public boolean isSaveEnchantChance()
	{
		if(isBlank())
			return false;

		return this.optionIndex == ENCHANT_SAVE_ENCHANT_LEVEL.getOptionIndex() && 100 * Rnd.nextDouble() < ENCHANT_SAVE_ENCHANT_LEVEL.getChance();
	}

	public boolean hasChancePlusTwo()
	{
		if(isBlank())
			return false;

		return this.optionIndex == ENCHANT_LEVEL_PLUS_ONE_TWO.getOptionIndex() && 100 * Rnd.nextDouble() < ENCHANT_LEVEL_PLUS_ONE_TWO.getChance();
	}

	public boolean isBlank()
	{
		return this.optionIndex == -1;
	}
}
