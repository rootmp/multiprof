package l2s.dataparser.data.common;

import l2s.dataparser.data.annotations.value.IntValue;
import l2s.gameserver.geometry.Location;

public class Point3
{
	@IntValue(withoutName = true)
	public int x;
	@IntValue(withoutName = true)
	public int y;
	@IntValue(withoutName = true)
	public int z;
	
	@Override
  public String toString() 
  {
    return x+";"+y+";"+z;
  }
	
	public Location getLocation()
	{
		return new Location(x, y, z);
	}
}