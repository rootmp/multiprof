package l2s.gameserver.templates;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.geometry.Location;

/**
 * @author nexvill
 */

public class TeleportTemplate
{
	private final int _id;
	private int _itemId;
	private long _price;
	private final List<Location> _locations = new ArrayList<Location>();
	
	public TeleportTemplate(int id, int itemId, long price)
	{
		_id = id;
		_itemId = itemId;
		_price = price;
	}

	public int getId()
	{
		return _id;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public void setItemId(int itemId)
	{
		_itemId = itemId;
	}
	
	public long getPrice()
	{
		return _price;
	}

	public void setPrice(int price)
	{
		_price = price;
	}
	
	public void addLocation(Location loc)
	{
		_locations.add(loc);
	}

	public List<Location> getLocations()
	{
		return _locations;
		// return _locations.toArray(new Location[_locations.size()]);
	}
}
