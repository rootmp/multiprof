package l2s.authserver.network.gamecomm.gs2as;

import l2s.authserver.network.gamecomm.GameServer;
import l2s.authserver.network.gamecomm.ReceivablePacket;

public class PlayerLogout extends ReceivablePacket
{
	private String account;

	@Override
	protected boolean readImpl()
	{
		account = readS();
		return true;
	}

	@Override
	protected void runImpl()
	{
		GameServer gs = getGameServer();
		if (gs.isAuthed())
			gs.removeAccount(account);
	}
}
