package l2s.gameserver.network.l2.c2s.steadybox;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.steadybox.ExSteadyOneBoxUpdate;
import l2s.gameserver.templates.item.ItemTemplate;

/**
 * @author nexvill
 */
public class RequestExSteadyOpenBox implements IClientIncomingPacket
{
	private int _slotId, _l2CoinCount;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotId = packet.readD();
		_l2CoinCount = packet.readD();
		packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(_l2CoinCount > 0)
		{
			if(activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, _l2CoinCount))
			{
				activeChar.sendPacket(new ExSteadyOneBoxUpdate(activeChar, _slotId, false, true));
			}
		}
		else
			activeChar.sendPacket(new ExSteadyOneBoxUpdate(activeChar, _slotId, false, false));
	}
}
