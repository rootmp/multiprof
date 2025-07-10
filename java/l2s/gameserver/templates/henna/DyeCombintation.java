package l2s.gameserver.templates.henna;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 08.11.2021
 **/
public class DyeCombintation
{
	private final int slotOne;
	private final int slotTwo;
	private final long adena;
	private final double chance;
	private final int resultDyeId;

	public DyeCombintation(int slotOne, int slotTwo, long adena, double chance, int resultDyeId)
	{
		this.slotOne = slotOne;
		this.slotTwo = slotTwo;
		this.adena = adena;
		this.chance = chance;
		this.resultDyeId = resultDyeId;
	}

	public int getSlotOne()
	{
		return slotOne;
	}

	public int getSlotTwo()
	{
		return slotTwo;
	}

	public long getAdena()
	{
		return adena;
	}

	public double getChance()
	{
		return chance;
	}

	public int getResultDyeId()
	{
		return resultDyeId;
	}
}
