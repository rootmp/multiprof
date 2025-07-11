package l2s.gameserver.network.l2.c2s.blessing;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.blessing.ExBlessOptionPutItem;

public class RequestExBlessOptionPutItem implements IClientIncomingPacket
{
	private int _itemObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemObjId = packet.readD(); // item object id
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExBlessOptionPutItem(activeChar, _itemObjId));
	}
}