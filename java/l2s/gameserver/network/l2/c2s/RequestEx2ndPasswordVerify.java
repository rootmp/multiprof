package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.network.l2.GameClient;

public class RequestEx2ndPasswordVerify implements IClientIncomingPacket
{
	private String _password;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_password = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if (!Config.EX_SECOND_AUTH_ENABLED)
			return;

		client.getSecondaryAuth().checkPassword(_password, false);
	}
}
