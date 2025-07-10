package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.data.xml.holder.LimitedShopHolder;
import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class RequestExPurchaseLimitShopItemList implements IClientIncomingPacket
{
	private int _listId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_listId = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		LimitedShopHolder.getInstance().SeparateAndSend(_listId, activeChar);
	}
}