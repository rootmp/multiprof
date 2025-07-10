package l2s.gameserver.enums;

public enum SummonType
{
	Summon(1),
	TemplierGuard(1),
	NewUnicorn(1),
	AssassinShadow(3),
	ElementalGlobe(1),
	MotherTree(1),

	TreeofSephiroth(1);

	private int _count;

	SummonType(int count)
	{
		_count = count;
	}

	public int getCount()
	{
		return _count;
	}	
}
