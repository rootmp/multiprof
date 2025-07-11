package l2s.gameserver.network.l2.c2s.steadybox;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.templates.item.ItemTemplate;

/**
 * @author nexvill
 */
public class RequestExSteadyOpenSlot implements IClientIncomingPacket
{
	private int _slotId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		switch (_slotId)
		{
			case 2:
			{
				if (activeChar.getInventory().reduceAdena(100_000_000L))
				{
					activeChar.setVar(PlayerVariables.SB_BOX_SLOTS, 2);
					activeChar.generateSteadyBox(false);
				}
				break;
			}
			case 3:
			{
				if (activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, 2000))
				{
					activeChar.setVar(PlayerVariables.SB_BOX_SLOTS, 3);
					activeChar.generateSteadyBox(false);
				}
				break;
			}
			case 4:
			{
				if (activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, 8000))
				{
					activeChar.setVar(PlayerVariables.SB_BOX_SLOTS, 4);
					activeChar.generateSteadyBox(false);
				}
				break;
			}
		}
	}
}
