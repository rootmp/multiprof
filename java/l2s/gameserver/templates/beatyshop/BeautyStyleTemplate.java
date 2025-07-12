package l2s.gameserver.templates.beatyshop;

import java.util.Collection;

import gnu.trove.map.TIntObjectMap;

public class BeautyStyleTemplate
{
	private final int _id;
	private final int _value;
	private final long _adena;
	private final long _coins;
	private final long _resetPrice;
	private final TIntObjectMap<BeautyColorTemplate> _colors;

	public BeautyStyleTemplate(final int id, final int value, final long adena, final long coins, final long resetPrice, final TIntObjectMap<BeautyColorTemplate> colors)
	{
		_id = id;
		_value = value;
		_adena = adena;
		_coins = coins;
		_resetPrice = resetPrice;
		_colors = colors;
	}

	public int getId()
	{
		return _id;
	}

	public int getValue()
	{
		return _value;
	}

	public long getAdena()
	{
		return _adena;
	}

	public long getCoins()
	{
		return _coins;
	}

	public long getResetPrice()
	{
		return _resetPrice;
	}

	public Collection<BeautyColorTemplate> getColors()
	{
		return _colors.valueCollection();
	}

	public BeautyColorTemplate getColor(final int id)
	{
		return _colors.get(id);
	}
}
