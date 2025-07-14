package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestTeleportBookMark implements IClientIncomingPacket
{
	private int slot;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		slot = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar != null)
			activeChar.getBookMarkList().tryTeleport(slot);
	}
}