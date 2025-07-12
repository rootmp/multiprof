package l2s.gameserver.templates.skill.enchant;

public class CouponSetting
{
	private final int couponId;
	private final int grade;
	private final int sublevel;
	private final int lcoinId;
	private final long lcoinCount;
	private final long adena;

	public CouponSetting(int couponId, int grade, int sublevel, int lcoinId, long lcoinCount, long adena)
	{
		this.couponId = couponId;
		this.grade = grade;
		this.sublevel = sublevel;
		this.lcoinId = lcoinId;
		this.lcoinCount = lcoinCount;
		this.adena = adena;
	}

	public int getCouponId()
	{
		return couponId;
	}

	public int getGrade()
	{
		return grade;
	}

	public int getSublevel()
	{
		return sublevel;
	}

	public int getLcoinId()
	{
		return lcoinId;
	}

	public long getLcoinCount()
	{
		return lcoinCount;
	}

	public long getAdena()
	{
		return adena;
	}
}
