package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ExEnchantOneRemoveFail;
import l2s.gameserver.network.l2.s2c.ExEnchantOneRemoveOK;

/**
 * @author Bonux
 **/
public class RequestNewEnchantRemoveOne implements IClientIncomingPacket
{
	private int _item1ObjectId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_item1ObjectId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		final ItemInstance item1 = activeChar.getInventory().getItemByObjectId(_item1ObjectId);
		if (item1 == null)
		{
			activeChar.sendPacket(ExEnchantOneRemoveFail.STATIC);
			return;
		}

		if (activeChar.getSynthesisItem1() != item1)
		{
			activeChar.sendPacket(ExEnchantOneRemoveFail.STATIC);
			return;
		}

		activeChar.setSynthesisItem1(null);
		activeChar.sendPacket(ExEnchantOneRemoveOK.STATIC);
	}
}