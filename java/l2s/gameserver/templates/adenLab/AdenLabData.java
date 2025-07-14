package l2s.gameserver.templates.adenLab;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdenLabData
{
	private int nBossID;
	private int nCurrentSlot;
	private int openCards;
	private int nTranscendEnchant;
	private int nNormalGameSaleDailyCount;
	private int nNormalGameDailyCount;
	private Map<Integer, int[]> specialSlots;

	public AdenLabData(int nBossID, int nCurrentSlot, int openCards, int nTranscendEnchant, int nNormalGameSaleDailyCount, int nNormalGameDailyCount, Map<Integer, int[]> specialSlots)
	{
		this.nBossID = nBossID;
		this.nCurrentSlot = nCurrentSlot;
		this.openCards = openCards;
		this.nTranscendEnchant = nTranscendEnchant;
		this.nNormalGameSaleDailyCount = nNormalGameSaleDailyCount;
		this.nNormalGameDailyCount = nNormalGameDailyCount;
		this.specialSlots = specialSlots;
	}

	public AdenLabData(int nBossID)
	{
		this.nBossID = nBossID;
		this.nCurrentSlot = 1;
		this.openCards = 0;
		this.nTranscendEnchant = 0;
		this.nNormalGameSaleDailyCount = 0;
		this.nNormalGameDailyCount = 0;
		this.specialSlots = new HashMap<>();
	}

	public int getBossID()
	{
		return nBossID;
	}

	public void setBossID(int nBossID)
	{
		this.nBossID = nBossID;
	}

	public int getCurrentSlot()
	{
		return nCurrentSlot;
	}

	public void setCurrentSlot(int nCurrentSlot)
	{
		this.nCurrentSlot = nCurrentSlot;
	}

	public int getTranscendEnchant()
	{
		return nTranscendEnchant;
	}

	public void setTranscendEnchant(int nTranscendEnchant)
	{
		this.nTranscendEnchant = nTranscendEnchant;
	}

	public int getNormalGameSaleDailyCount()
	{
		return nNormalGameSaleDailyCount;
	}

	public void setNormalGameSaleDailyCount(int nNormalGameSaleDailyCount)
	{
		this.nNormalGameSaleDailyCount = nNormalGameSaleDailyCount;
	}

	public int getNormalGameDailyCount()
	{
		return nNormalGameDailyCount;
	}

	public void setNormalGameDailyCount(int nNormalGameDailyCount)
	{
		this.nNormalGameDailyCount = nNormalGameDailyCount;
	}

	public int getOpenCards()
	{
		return openCards;
	}

	public void resetOpenCards()
	{
		openCards = 0;
	}

	public void setOpenCards(int i)
	{
		openCards = i;
	}

	public void setSpecialSlot(int slot, int[] options)
	{
		specialSlots.put(slot, options);
	}

	public int[] getSpecialSlot(int slot)
	{
		if(!specialSlots.containsKey(slot))
			return new int[0];

		return Arrays.stream(specialSlots.get(slot)).filter(s -> s > 0).toArray();
	}

	public Map<Integer, int[]> getSpecialSlots()
	{
		return specialSlots;
	}
}
