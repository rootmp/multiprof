package l2s.commons.ban;

/**
 * @author Bonux (Head Developer L2-scripts.com) 10.04.2019 Developed for
 *         L2-Scripts.com
 **/
public enum BanBindType
{
	LOGIN(true, true),
	IP(true, true),
	HWID(true, true),
	PLAYER(false, true),
	CHAT(false, true);

	public static final BanBindType[] VALUES = values();

	private final boolean _auth;
	private final boolean _game;

	BanBindType(boolean auth, boolean game)
	{
		_auth = auth;
		_game = game;
	}

	public boolean isAuth()
	{
		return _auth;
	}

	public boolean isGame()
	{
		return _game;
	}
}
