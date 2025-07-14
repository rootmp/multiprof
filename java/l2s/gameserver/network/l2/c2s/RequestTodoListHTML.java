package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

/**
 * @author Bonux
 **/
public class RequestTodoListHTML implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int _tab;
	@SuppressWarnings("unused")
	private String _linkName;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_tab = packet.readC();
		_linkName = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		// TODO
	}
}