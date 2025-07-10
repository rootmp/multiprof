package l2s.commons.ban;

/**
 * @author Bonux (Head Developer L2-scripts.com) 11.04.2019 Developed for
 *         L2-Scripts.com
 **/
public class BanInfo
{
	private final int _endTime;
	private final String _reason;

	public BanInfo(int endTime, String reason)
	{
		_endTime = endTime;
		_reason = reason;
	}

	public int getEndTime()
	{
		return _endTime;
	}

	public String getReason()
	{
		return _reason;
	}
}
