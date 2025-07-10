package l2s.gameserver.network.l2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import l2s.commons.dbutils.DbUtils;
import l2s.commons.network.ChannelInboundHandler;
import l2s.commons.network.IConnectionState;
import l2s.commons.network.ICrypt;
import l2s.commons.network.IIncomingPacket;
import l2s.gameserver.Config;
import l2s.gameserver.GameServer;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.config.FloodProtectorConfig;
import l2s.gameserver.config.FloodProtectorConfigs;
import l2s.gameserver.config.xml.holder.HostsConfigHolder;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.CharSelectInfoPackage;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.network.authcomm.SessionKey;
import l2s.gameserver.network.authcomm.gs2as.PlayerLogout;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.components.hwid.HwidHolder;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.network.l2.s2c.LogOutOkPacket;
import l2s.gameserver.network.l2.s2c.NetPingPacket;
import l2s.gameserver.network.l2.s2c.ServerClose;
import l2s.gameserver.security.SecondaryPasswordAuth;
import l2s.gameserver.utils.Language;


/**
 * Represents a client connected on Game Server
 */
public final class GameClient extends ChannelInboundHandler<GameClient>
{
	private static final Logger logger = LoggerFactory.getLogger(GameClient.class);
	
	private Channel _channel;
	public GameCrypt _crypt = null;

	private final Map<String, FloodProtector> _floodProtectors = new HashMap<String, FloodProtector>();

	private InetAddress _addr;

	/** Данные аккаунта */
	private String _login;
	private int _premiumAccountType = 0;
	private int _premiumAccountExpire;
	private int _points = 0;
	private Language _language = Config.DEFAULT_LANG;
	private long _phoneNumber = 0L;

	private Player _activeChar;
	private SessionKey _sessionKey;
	private int revision = 0;

	private SecondaryPasswordAuth _secondaryAuth = null;

	private List<Integer> _charSlotMapping = new ArrayList<Integer>();
	private HwidHolder hwidHolder = null;
	
	public static int DEFAULT_PAWN_CLIPPING_RANGE = 2048;
	private int _pingTimestamp;
	private int _ping;
	private int _fps;
	private int _pawnClippingRange;
	private ScheduledFuture<?> _pingTaskFuture;
	
	private volatile boolean _isDetached = false;
	private boolean isAuthed = false;
	
	public GameClient()
	{
		_crypt = new GameCrypt();
		for(FloodProtectorConfig config : FloodProtectorConfigs.FLOOD_PROTECTORS)
			_floodProtectors.put(config.FLOOD_PROTECTOR_TYPE, new FloodProtector(this, config));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
		super.channelActive(ctx);

		setConnectionState(ConnectionState.CONNECTED);
		final InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		_addr = address.getAddress();
		_channel = ctx.channel();

		for(FloodProtectorConfig config : FloodProtectorConfigs.FLOOD_PROTECTORS)
			_floodProtectors.put(config.FLOOD_PROTECTOR_TYPE, new FloodProtector(this, config));

		logger.debug("Client Connected: {}", _channel);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx)
	{
		logger.debug("Client Disconnected: {}", _channel);

		if(_pingTaskFuture != null)
		{
			_pingTaskFuture.cancel(true);
			_pingTaskFuture = null;
		}
		
		final Player player;
		setConnectionState(ConnectionState.DISCONNECTED);
		player = getActiveChar();
		
		if(player != null&& !isLogout() && player.getAutoFarm().isFarmActivate() && player.hasPremiumAccount() && !player.isInOfflineMode())
		{
			//player.offlineAF();
		}
		
		setActiveChar(null);

		if(player != null)
		{
			player.setNetConnection(null);
			player.scheduleDelete();
		}

		if(getSessionKey() != null)
		{
			if(isAuthed())
			{
				AuthServerCommunication.getInstance().removeAuthedClient(getLogin());
				AuthServerCommunication.getInstance().sendPacket(new PlayerLogout(getLogin()));
			}
			else
			{
				AuthServerCommunication.getInstance().removeWaitingClient(getLogin());
			}
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IIncomingPacket<GameClient> packet)
	{
		ThreadPoolManager.getInstance().execute(() -> {
			try
			{
				packet.run(GameClient.this);
			}
			catch (Exception e)
			{
				logger.warn("Exception for: {} on packet.run: {}", toString(), packet.getClass().getSimpleName(), e);
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		if (cause instanceof IOException) 
		{
			if (cause.getMessage().equals("Connection reset by peer")) 
			{
				 logger.debug("Network exception caught for: {}", toString());
				ctx.close();
				return;
			}

			if (cause.getMessage().equals("An existing connection was forcibly closed by the remote host")) 
			{
				 logger.debug("Network exception caught for: {}", toString());
				ctx.close();
				return;
			}
		}

		 logger.warn("Network exception caught for: {}", toString());
	}
	
	public boolean isAuthed() 
	{
		return isAuthed;
	}

	public void setAuthed(boolean authed) 
	{
		isAuthed = authed;
	}
	
	public ICrypt getCrypt()
	{
		return _crypt;
	}
	
	public boolean isConnected()
	{
		final Channel conn = _channel;
		return conn != null && conn.isActive();
	}

	
	public void markRestoredChar(int charslot) throws Exception
	{
		int objid = getObjectIdForSlot(charslot);
		if(objid < 0)
			return;

		if(_activeChar != null && _activeChar.getObjectId() == objid)
			_activeChar.setDeleteTimer(0);

		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET deletetime=0 WHERE obj_id=?");
			statement.setInt(1, objid);
			statement.execute();
		}
		catch(Exception e)
		{
			logger.error("Exception while marking restored character for deletion", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void markToDeleteChar(int charslot) throws Exception
	{
		int objid = getObjectIdForSlot(charslot);
		if(objid < 0)
			return;

		if(_activeChar != null && _activeChar.getObjectId() == objid)
			_activeChar.setDeleteTimer((int) (System.currentTimeMillis() / 1000));

		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET deletetime=? WHERE obj_id=?");
			statement.setLong(1, (int) (System.currentTimeMillis() / 1000L));
			statement.setInt(2, objid);
			statement.execute();
		}
		catch(Exception e)
		{
			logger.error("Exception while marking character for deletion", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void deleteChar(int charslot) throws Exception
	{
		//have to make sure active character must be nulled
		if(_activeChar != null)
			return;

		int objid = getObjectIdForSlot(charslot);
		if(objid == -1)
			return;

		CharacterDAO.getInstance().deleteCharByObjId(objid);
	}

	public Player loadCharFromDisk(int charslot)
	{
		int objectId = getObjectIdForSlot(charslot);
		if(objectId == -1)
			return null;

		Player character = null;
		Player oldPlayer = GameObjectsStorage.getPlayer(objectId);

		if(oldPlayer != null)
		{
			if(oldPlayer.isInOfflineMode() || oldPlayer.isLogoutStarted())
			{
				// оффтрейдового чара проще выбить чем восстанавливать
				oldPlayer.kick();
			}
			else
			{
				oldPlayer.sendPacket(SystemMsg.ANOTHER_PERSON_HAS_LOGGED_IN_WITH_THE_SAME_ACCOUNT);

				GameClient oldClient = oldPlayer.getNetConnection();
				if(oldClient != null)
				{
					oldClient.setActiveChar(null);
					oldClient.closeNow();
				}
				oldPlayer.setNetConnection(this);
				character = oldPlayer;
			}
		}
		
		if(getHwidHolder()!=null)
		{
			List<Player> adPlayers = GameObjectsStorage.getOfflinePlayers().stream().filter(p-> !p.getLastHwid().isEmpty()&& p.isInOfflineMode()&& p.getLastHwid().equalsIgnoreCase(getHwidHolder().asString())).collect(Collectors.toList());
			for(Player p:adPlayers)
			{
				p.kick();
			}
		}
		
		if(character == null)
			character = Player.restore(objectId, hwidHolder);

		if(character != null)
			setActiveChar(character);
		else
			logger.warn("Could not restore obj_id: {} in slot: {}", objectId, charslot);

		return character;
	}

	public void closeNow()
	{
		if (_channel != null)
		{
			_channel.close();
		}
	}
	
	public void close(IClientOutgoingPacket packet)
	{
		sendPacket(packet);
		closeNow();
	}

	public void close(boolean toLoginScreen)
	{
		close(toLoginScreen ? ServerClose.STATIC_PACKET : LogOutOkPacket.STATIC);
	}
	
	public int getObjectIdForSlot(int charslot)
	{
		if(charslot < 0 || charslot >= _charSlotMapping.size())
		{
			logger.warn("{} tried to modify Character in slot {} but no characters exist at that slot.", getLogin(), charslot);
			return -1;
		}
		return _charSlotMapping.get(charslot);
	}

	public Player getActiveChar()
	{
		return _activeChar;
	}

	public void setHwidHolder(HwidHolder hwidHolder)
	{
		this.hwidHolder = hwidHolder;
	}

	public HwidHolder getHwidHolder()
	{
		return hwidHolder;
	}

	public String getHwidString()
	{
		return hwidHolder.asString();
	}

	/**
	 * @return Returns the sessionId.
	 */
	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}

	public String getLogin()
	{
		return _login;
	}

	public void setLoginName(String loginName)
	{
		_login = loginName;

		if(Config.EX_SECOND_AUTH_ENABLED)
			_secondaryAuth = new SecondaryPasswordAuth(this);
	}

	public void setActiveChar(Player player)
	{
		_activeChar = player;
		if(player != null)
			player.setNetConnection(this);
	}

	public void setSessionId(SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}

	public void setCharSelection(CharSelectInfoPackage[] chars)
	{
		_charSlotMapping.clear();

		for(CharSelectInfoPackage element : chars)
		{
			int objectId = element.getObjectId();
			_charSlotMapping.add(objectId);
		}
	}

	public void setCharSelection(int c)
	{
		_charSlotMapping.clear();
		_charSlotMapping.add(c);
	}

	public int getRevision()
	{
		return revision;
	}

	public void setRevision(int revision)
	{
		this.revision = revision;
	}

	public boolean checkFloodProtection(String type, String command)
	{
		FloodProtector floodProtector = _floodProtectors.get(type.toUpperCase());
		return floodProtector == null || floodProtector.tryPerformAction(command);
	}

	public void sendPacket(IClientOutgoingPacket packet)
	{
		if (_isDetached || packet == null)
		{
			return;
		}

		// Write into the channel.
		_channel.writeAndFlush(packet);

		// Run packet implementation.
		packet.runImpl(getActiveChar());
	}

	public String getIpAddr()
	{
		return _addr.getHostAddress();
	}

	public int getServerId()
	{
		return HostsConfigHolder.getInstance().getServerId(getIpAddr());
	}

	
	public byte[] enableCrypt()
	{
		byte[] key = BlowFishKeygen.getRandomKey();
		_crypt.setKey(key);
		return key;
	}

	public boolean hasPremiumAccount()
	{
		return _premiumAccountType != 0 && _premiumAccountExpire > System.currentTimeMillis() / 1000L;
	}

	public void setPremiumAccountType(int type)
	{
		_premiumAccountType = type;
	}

	public int getPremiumAccountType()
	{
		return _premiumAccountType;
	}

	public void setPremiumAccountExpire(int expire)
	{
		_premiumAccountExpire = expire;
	}

	public int getPremiumAccountExpire()
	{
		return _premiumAccountExpire;
	}

	public int getPoints()
	{
		return _points;
	}

	public void setPoints(int points)
	{
		_points = points;
	}

	public Language getLanguage()
	{
		return _language;
	}

	public void setLanguage(Language language)
	{
		_language = language;
	}

	public long getPhoneNumber()
	{
		return _phoneNumber;
	}

	public void setPhoneNumber(long value)
	{
		_phoneNumber = value;
	}

	public SecondaryPasswordAuth getSecondaryAuth()
	{
		return _secondaryAuth;
	}

	@Override
	public String toString()
	{
		try
		{
			final InetAddress address = _addr;
			IConnectionState state = getConnectionState();
			final Player player = getActiveChar();
			if (ConnectionState.CONNECTED.equals(state)) {
				return "[IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
			} else if (ConnectionState.AUTHENTICATED.equals(state)) {
				return "[Account: " + getLogin() + " - IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
			} else if (ConnectionState.IN_GAME.equals(state) || ConnectionState.JOINING_GAME.equals(state)) {
				return "[Character: " + (player == null ? "disconnected" : player)
						+ " - Account: " + getLogin()
						+ " - IP: " + (address == null ? "disconnected" : address.getHostAddress()) + "]";
			}
			throw new IllegalStateException("Missing state on switch: " + state);
		}
		catch (NullPointerException e)
		{
			return "[Character read failed due to disconnect]";
		}
	}

	public boolean secondaryAuthed()
	{
		if(!Config.EX_SECOND_AUTH_ENABLED)
			return true;

		return getSecondaryAuth().isAuthed();
	}

	public int getSlotForObjectId(final int objectId)
	{
		final List <Integer> charSlotMapping = _charSlotMapping;
		return IntStream.range(0, charSlotMapping.size()).filter(slotIdx->Integer.valueOf(objectId).equals(charSlotMapping.get(slotIdx))).findFirst().orElse(-1);
	}

	public void onPing(final int timestamp, final int fps, final int pawnClipRange)
	{
		if(_pingTimestamp == 0 || _pingTimestamp == timestamp)
		{
			final long nowMs = System.currentTimeMillis();
			final long serverStartTimeMs = GameServer.getInstance().getServerStartTime();
			_ping = ((_pingTimestamp > 0) ? ((int) (nowMs - serverStartTimeMs - timestamp)) : 0);
			_fps = fps;
			_pawnClippingRange = pawnClipRange;
			_pingTaskFuture = ThreadPoolManager.getInstance().schedule(new PingTask(this), 30000L);
		}
	}
	
	private void doPing()
	{
		final long nowMs = System.currentTimeMillis();
		final long serverStartTimeMs = GameServer.getInstance().getServerStartTime();
		final int timestamp = (int) (nowMs - serverStartTimeMs);
		_pingTimestamp = timestamp;
		sendPacket(new NetPingPacket(timestamp));
	}

	public int getPing()
	{
		return _ping;
	}

	public int getFps()
	{
		return _fps;
	}

	
	public int getPawnClippingRange()
	{
		return _pawnClippingRange;
	}
	
	private static class PingTask implements Runnable
	{
		private final GameClient _client;

		private PingTask(final GameClient client)
		{
			_client = client;
		}

		@Override
		public void run()
		{
			if(_client == null || !_client.isConnected())
			 return; 
			_client.doPing();
		}

	}
	
	private boolean isLogout = false;
	
	public void setLogout(boolean b)
	{
		isLogout = b;
	}
	public boolean isLogout()
	{
		return isLogout;
	}
}