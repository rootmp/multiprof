package l2s.gameserver.network.l2.c2s.itemrestore;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExItemRestore implements IClientIncomingPacket
{
	private int nBrokenItemClassID;
	private int cEnchant;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nBrokenItemClassID = packet.readD();
		cEnchant = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		activeChar.getEnchantBrokenItemList().restoreItem(nBrokenItemClassID, cEnchant);
	}
}