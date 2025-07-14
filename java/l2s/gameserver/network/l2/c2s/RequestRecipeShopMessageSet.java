package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestRecipeShopMessageSet implements IClientIncomingPacket
{
	// format: cS
	private String _name;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_name = packet.readS(16);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		activeChar.setManufactureName(_name);
		activeChar.storePrivateStore();
		activeChar.broadcastPrivateStoreInfo();
	}
}