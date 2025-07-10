package l2s.gameserver.network.l2.c2s.spectating;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;

/**
 * @author nexvill
 */
public class RequestExUserWatcherDelete implements IClientIncomingPacket
{
	String _name;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_name = readString();
		packet.readD(); // 0
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.getSpectatingList().remove(_name);
	}
}
