package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.commons.network.PacketReader;
import l2s.dataparser.data.common.ItemRequiredId;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.ActionFailPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.newhenna.NewHennaUnequip;
import l2s.gameserver.templates.item.henna.Henna;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExNewHennaUnequip implements IClientIncomingPacket
{
	private int _slotId;
	private int _nCostItemId;
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotId = packet.readC();
		_nCostItemId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		Henna henna;
		if ((_slotId == 1) || (_slotId == 2) || (_slotId == 3) || (_slotId == 4))
		{
			henna = player.getHenna(_slotId);

			ItemRequiredId _cancelFee = henna.getCancelFee(_nCostItemId);

			if (_cancelFee!=null && ItemFunctions.haveItem(player, _cancelFee.itemName, _cancelFee.count))//добавить возможность бесплатного снятия?
			{
				player.removeHenna(_slotId);
				ItemFunctions.deleteItem(player, _cancelFee.itemName, _cancelFee.count);

				if (henna.getCancelCount() > 0)
					ItemFunctions.addItem(player, henna.getDyeItemId(), henna.getCancelCount());

				player.sendPacket(new SystemMessage(SystemMessage.THE_SYMBOL_HAS_BEEN_DELETED));


				player.sendPacket(new NewHennaUnequip(_slotId, 1));
				player.applyDyePotenSkills();
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
				player.sendPacket(ActionFailPacket.STATIC);
				player.sendPacket(new NewHennaUnequip(_slotId, 0));
				player.updateStatBonus();
			}
		}
		else
		{
			player.sendPacket(ActionFailPacket.STATIC);
			player.sendPacket(new NewHennaUnequip(_slotId, 0));
		}
	}


}
