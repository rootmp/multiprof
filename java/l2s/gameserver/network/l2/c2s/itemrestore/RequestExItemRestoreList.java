package l2s.gameserver.network.l2.c2s.itemrestore;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;

public class RequestExItemRestoreList implements IClientIncomingPacket
{
	private int cCategory;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cCategory = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.getEnchantBrokenItemList().showRestoreWindowCategory(cCategory);
	}
}