package l2s.authserver.network.l2.c2s;

import l2s.authserver.Config;
import l2s.authserver.GameServerManager;
import l2s.authserver.accounts.Account;
import l2s.authserver.network.gamecomm.GameServer;
import l2s.authserver.network.l2.L2LoginClient;
import l2s.authserver.network.l2.SessionKey;
import l2s.authserver.network.l2.s2c.PlayFail;
import l2s.authserver.network.l2.s2c.PlayOk;

/**
 * Fromat is ddc d: first part of session id d: second part of session id c:
 * server ID
 */
public class RequestServerLogin extends L2LoginClientPacket
{
	private int _loginOkID1;
	private int _loginOkID2;
	private int _serverId;

	@Override
	protected boolean readImpl()
	{
		_loginOkID1 = readD();
		_loginOkID2 = readD();
		_serverId = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		L2LoginClient client = getClient();
		if(!client.isPasswordCorrect())
		{
			client.close(PlayFail.REASON_USER_OR_PASS_WRONG);
			return;
		}

		SessionKey skey = client.getSessionKey();
		if(skey == null || Config.SHOW_LICENCE && !skey.checkLoginPair(_loginOkID1, _loginOkID2))
		{
			client.close(PlayFail.REASON_ACCESS_FAILED);
			return;
		}

		Account account = client.getAccount();
		GameServer gs = GameServerManager.getInstance().getGameServerById(_serverId);
		if(gs == null || !gs.isAuthed())
		{
			client.close(PlayFail.REASON_ACCESS_FAILED);
			return;
		}

		if(gs.isGmOnly() && account.getAccessLevel() < 100)
		{
			client.close(PlayFail.REASON_SERVER_MAINTENANCE);
			return;
		}

		if(gs.getOnline() >= gs.getMaxPlayers() && account.getAccessLevel() < 50)
		{
			client.close(PlayFail.REASON_SERVER_OVERLOADED);
			return;
		}

		account.setLastServer(_serverId);
		account.update();

		client.close(new PlayOk(skey, _serverId));
	}
}