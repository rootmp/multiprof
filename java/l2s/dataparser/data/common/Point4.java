package l2s.dataparser.data.common;

import l2s.dataparser.data.annotations.value.IntValue;

public class Point4
{
	@IntValue(withoutName = true)
	public int x;
	@IntValue(withoutName = true)
	public int y;
	@IntValue(withoutName = true)
	public int zMin;
	@IntValue(withoutName = true)
	public int zMax;

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZMin()
	{
		return zMin;
	}

	public int getZMax()
	{
		return zMax;
	}
}