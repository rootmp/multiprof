package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class RequestUpdateFriendMemo implements IClientIncomingPacket
{
	private String _name;
	private String _memo;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_name = packet.readS();
		_memo = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.getFriendList().updateMemo(_name, _memo);
	}
}