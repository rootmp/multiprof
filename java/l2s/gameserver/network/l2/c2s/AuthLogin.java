package l2s.gameserver.network.l2.c2s;

import java.nio.BufferUnderflowException;

import org.apache.commons.lang3.ArrayUtils;

import l2s.commons.ban.BanBindType;
import l2s.commons.network.PacketReader;
import l2s.gameserver.GameServer;
import l2s.gameserver.Shutdown;
import l2s.gameserver.instancemanager.AuthBanManager;
import l2s.gameserver.instancemanager.GameBanManager;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.network.authcomm.SessionKey;
import l2s.gameserver.network.authcomm.gs2as.PlayerAuthRequest;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.hwid.DefaultHwidHolder;
import l2s.gameserver.network.l2.components.hwid.EmptyHwidHolder;
import l2s.gameserver.network.l2.s2c.LoginResultPacket;
import l2s.gameserver.network.l2.s2c.ServerCloseSocketPacket;
import l2s.gameserver.utils.Language;

/**
 * cSddddd
 * cSdddddQ
 * loginName + keys must match what the loginserver used.
 */
public class AuthLogin implements IClientIncomingPacket
{
	private static final int HWID_LENGTH = 32;

	private byte[] hwid = ArrayUtils.EMPTY_BYTE_ARRAY;
	private String _loginName;
	private int _playKey1;
	private int _playKey2;
	private int _loginKey1;
	private int _loginKey2;
	private int _lang;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_loginName = packet.readS(32).toLowerCase();
		_playKey2 = packet.readD();
		_playKey1 = packet.readD();
		_loginKey1 = packet.readD();
		_loginKey2 = packet.readD();
		_lang = packet.readD();
		packet.readQ();
		packet.readD(); // UNK

		if(!GameServer.DEVELOP && packet.getReadableBytes() >= HWID_LENGTH)
		{
			try
			{
				hwid = new byte[HWID_LENGTH];
				packet.readB(hwid, packet.getReadableBytes() - HWID_LENGTH, HWID_LENGTH);
			}
			catch(BufferUnderflowException ex)
			{
				client.closeNow();
				return false;
			}
		}

		return true;
	}

	@Override
	public void run(GameClient client)
	{
		SessionKey key = new SessionKey(_loginKey1, _loginKey2, _playKey1, _playKey2);
		client.setSessionId(key);
		client.setLoginName(_loginName);
		client.setLanguage(Language.getLanguage(_lang));

		if(GameServer.DEVELOP)
			client.setHwidHolder(new EmptyHwidHolder(_loginName));
		else
			client.setHwidHolder(new DefaultHwidHolder(hwid));

		if(Shutdown.getInstance().getMode() != Shutdown.NONE && Shutdown.getInstance().getSeconds() <= 15)
			client.closeNow();
		else
		{
			if(AuthServerCommunication.getInstance().isShutdown())
			{
				client.close(LoginResultPacket.SYSTEM_ERROR_LOGIN_LATER);
				return;
			}
			if(GameBanManager.getInstance().isBanned(BanBindType.LOGIN, client.getLogin()))
			{
				client.close(LoginResultPacket.ACCESS_FAILED_TRY_LATER);
				return;
			}
			if(GameBanManager.getInstance().isBanned(BanBindType.IP, client.getIpAddr()))
			{
				client.close(LoginResultPacket.ACCESS_FAILED_TRY_LATER);
				return;
			}
			if(AuthBanManager.getInstance().isBanned(BanBindType.HWID, client.getHwidHolder().asString())
					|| GameBanManager.getInstance().isBanned(BanBindType.HWID, client.getHwidHolder().asString()))
			{
				client.close(LoginResultPacket.ACCESS_FAILED_TRY_LATER);
				return;
			}

			GameClient oldClient = AuthServerCommunication.getInstance().addWaitingClient(client);
			if(oldClient != null)
				oldClient.close(ServerCloseSocketPacket.STATIC);

			AuthServerCommunication.getInstance().sendPacket(new PlayerAuthRequest(client));
		}
	}
}