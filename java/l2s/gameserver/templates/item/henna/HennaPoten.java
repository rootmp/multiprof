package l2s.gameserver.templates.item.henna;

/**
 * @author Serenitty
 */
public class HennaPoten
{
	private Henna _henna;
	private int _potenId;
	private int _enchantLevel = 1;
	private int _enchantExp;

	public HennaPoten()
	{}

	public void setHenna(Henna henna)
	{
		_henna = henna;
	}

	public Henna getHenna()
	{
		return _henna;
	}

	public void setPotenId(int val)
	{
		_potenId = val;
	}

	public int getPotenId()
	{
		return _potenId;
	}

	public void setEnchantLevel(int val)
	{
		_enchantLevel = val;
	}

	public int getEnchantLevel()
	{
		return _enchantLevel;
	}

	public void setEnchantExp(int val)
	{
		_enchantExp = val;
	}

	public int getEnchantExp()
	{
		return _enchantExp;
	}

	public boolean isPotentialAvailable()
	{
		return (_henna != null) && (_henna.getPatternLevel() > 0);
	}

	public int getActiveStep()
	{
		if(!isPotentialAvailable())
			return 0;
		if(_enchantLevel == 1)
			return 0;
		return Math.min(_enchantLevel - 1, _henna.getPatternLevel());
	}
}