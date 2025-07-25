package l2s.gameserver.templates;

public class VariableContainer
{
	private final String _name;
	private final String _value;
	private final long _expireTime;

	public VariableContainer(String name, String value, long expireTime)
	{
		_name = name;
		_value = value;
		_expireTime = expireTime;
	}

	public String getName()
	{
		return _name;
	}

	public String getValue()
	{
		return _value;
	}

	public long getExpireTime()
	{
		return _expireTime;
	}

	public boolean isExpired()
	{
		return _expireTime > 0 && _expireTime < System.currentTimeMillis();
	}
}