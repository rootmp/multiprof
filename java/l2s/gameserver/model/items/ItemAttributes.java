package l2s.gameserver.model.items;

import l2s.gameserver.model.base.Element;

public class ItemAttributes
{
	private int _fire;
	private int _water;
	private int _wind;
	private int _earth;
	private int _holy;
	private int _unholy;

	public ItemAttributes()
	{
		this(0, 0, 0, 0, 0, 0);
	}

	public ItemAttributes(int fire, int water, int wind, int earth, int holy, int unholy)
	{
		_fire = fire;
		_water = water;
		_wind = wind;
		_earth = earth;
		_holy = holy;
		_unholy = unholy;
	}

	public int getFire()
	{
		return _fire;
	}

	public void setFire(int fire)
	{
		_fire = fire;
	}

	public int getWater()
	{
		return _water;
	}

	public void setWater(int water)
	{
		_water = water;
	}

	public int getWind()
	{
		return _wind;
	}

	public void setWind(int wind)
	{
		_wind = wind;
	}

	public int getEarth()
	{
		return _earth;
	}

	public void setEarth(int earth)
	{
		_earth = earth;
	}

	public int getHoly()
	{
		return _holy;
	}

	public void setHoly(int holy)
	{
		_holy = holy;
	}

	public int getUnholy()
	{
		return _unholy;
	}

	public void setUnholy(int unholy)
	{
		_unholy = unholy;
	}

	public Element getElement()
	{
		if (_fire > 0)
		{
			return Element.FIRE;
		}
		else if (_water > 0)
		{
			return Element.WATER;
		}
		else if (_wind > 0)
		{
			return Element.WIND;
		}
		else if (_earth > 0)
		{
			return Element.EARTH;
		}
		else if (_holy > 0)
		{
			return Element.HOLY;
		}
		else if (_unholy > 0)
		{
			return Element.UNHOLY;
		}

		return Element.NONE;
	}

	public int getValue()
	{
		if (_fire > 0)
		{
			return _fire;
		}
		else if (_water > 0)
		{
			return _water;
		}
		else if (_wind > 0)
		{
			return _wind;
		}
		else if (_earth > 0)
		{
			return _earth;
		}
		else if (_holy > 0)
		{
			return _holy;
		}
		else if (_unholy > 0)
		{
			return _unholy;
		}

		return 0;
	}

	public void setValue(Element element, int value)
	{
		switch (element)
		{
			case FIRE:
				_fire = value;
				break;
			case WATER:
				_water = value;
				break;
			case WIND:
				_wind = value;
				break;
			case EARTH:
				_earth = value;
				break;
			case HOLY:
				_holy = value;
				break;
			case UNHOLY:
				_unholy = value;
				break;
		}
	}

	public int getValue(Element element)
	{
		switch (element)
		{
			case FIRE:
				return _fire;
			case WATER:
				return _water;
			case WIND:
				return _wind;
			case EARTH:
				return _earth;
			case HOLY:
				return _holy;
			case UNHOLY:
				return _unholy;
			default:
				return 0;
		}
	}

	@Override
	public ItemAttributes clone()
	{
		return new ItemAttributes(_fire, _water, _wind, _earth, _holy, _unholy);
	}
}
