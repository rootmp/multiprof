package l2s.authserver.network.gamecomm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import l2s.commons.net.HostInfo;

/**
 * @reworked by Bonux
 **/
public class GameServer
{
	private final Map<Integer, HostInfo> _hosts = new HashMap<>();
	private int _serverType;
	private int _ageLimit;
	private int _protocol;
	private boolean _isOnline;
	private boolean _isPvp;
	private boolean _isShowingBrackets;
	private boolean _isGmOnly;

	private int _maxPlayers;

	private GameServerConnection _conn;
	private boolean _isAuthed;

	private Set<String> _accounts = new CopyOnWriteArraySet<String>();

	public GameServer(GameServerConnection conn)
	{
		_conn = conn;
	}

	public GameServer(int id, String ip, int port, String key)
	{
		_conn = null;
		addHost(new HostInfo(id, ip, port, key));
	}

	public void addHost(HostInfo host)
	{
		_hosts.put(host.getId(), host);
	}

	public HostInfo removeHost(int id)
	{
		return _hosts.remove(id);
	}

	public HostInfo getHost(int id)
	{
		return _hosts.get(id);
	}

	public HostInfo[] getHosts()
	{
		return _hosts.values().toArray(new HostInfo[0]);
	}

	public void setAuthed(boolean isAuthed)
	{
		_isAuthed = isAuthed;
	}

	public boolean isAuthed()
	{
		return _isAuthed;
	}

	public void setConnection(GameServerConnection conn)
	{
		_conn = conn;
	}

	public GameServerConnection getConnection()
	{
		return _conn;
	}

	public void setMaxPlayers(int maxPlayers)
	{
		_maxPlayers = maxPlayers;
	}

	public int getMaxPlayers()
	{
		return _maxPlayers;
	}

	public int getOnline()
	{
		return _accounts.size();
	}

	public Set<String> getAccounts()
	{
		return _accounts;
	}

	public void addAccount(String account)
	{
		_accounts.add(account);
	}

	public void removeAccount(String account)
	{
		_accounts.remove(account);
	}

	public void setDown()
	{
		setAuthed(false);
		setConnection(null);
		setOnline(false);

		_accounts.clear();
	}

	public void sendPacket(SendablePacket packet)
	{
		GameServerConnection conn = getConnection();
		if (conn != null)
			conn.sendPacket(packet);
	}

	public int getServerType()
	{
		return _serverType;
	}

	public boolean isOnline()
	{
		return _isOnline;
	}

	public void setOnline(boolean online)
	{
		_isOnline = online;
	}

	public void setServerType(int serverType)
	{
		_serverType = serverType;
	}

	public boolean isPvp()
	{
		return _isPvp;
	}

	public void setPvp(boolean pvp)
	{
		_isPvp = pvp;
	}

	public boolean isShowingBrackets()
	{
		return _isShowingBrackets;
	}

	public void setShowingBrackets(boolean showingBrackets)
	{
		_isShowingBrackets = showingBrackets;
	}

	public boolean isGmOnly()
	{
		return _isGmOnly;
	}

	public void setGmOnly(boolean gmOnly)
	{
		_isGmOnly = gmOnly;
	}

	public int getAgeLimit()
	{
		return _ageLimit;
	}

	public void setAgeLimit(int ageLimit)
	{
		_ageLimit = ageLimit;
	}

	public int getProtocol()
	{
		return _protocol;
	}

	public void setProtocol(int protocol)
	{
		_protocol = protocol;
	}
}