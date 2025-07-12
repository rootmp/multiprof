package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Request;
import l2s.gameserver.model.Request.L2RequestType;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExApplyVariationOption;
import l2s.gameserver.network.l2.s2c.ExVariationCancelResult;
import l2s.gameserver.network.l2.s2c.ExVariationResult;
import l2s.gameserver.utils.VariationUtils;

public class RequestExApplyVariationOption implements IClientIncomingPacket
{
	private int _enchantedObjectId;
	private int _option1;
	private int _option2;
	private int _option3;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_enchantedObjectId = packet.readD();
		_option1 = packet.readD();
		_option2 = packet.readD();
		_option3 = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		ItemInstance targetItem = player.getInventory().getItemByObjectId(_enchantedObjectId);
		if(targetItem == null)
		{
			player.sendPacket(new ExVariationResult(0, 0, 0, 0));
			return;
		}

		Request request = player.getRequest();
		if(request == null || !request.isTypeOf(L2RequestType.REFINE_REQUEST))
		{
			player.sendPacket(new ExVariationResult(0, 0, 0, 0));
			return;
		}
		int option1Id = request.getInteger("variation1Id");
		int option2Id = request.getInteger("variation2Id");
		int option3Id = request.getInteger("variation3Id");
		int stoneId = request.getInteger("stoneId");

		if((targetItem.getObjectId() != _enchantedObjectId) || (_option1 != option1Id) || (_option2 != option2Id))
		{
			player.sendPacket(new ExApplyVariationOption(0, 0, 0, 0, 0));
			return;
		}

		// Remove the augmentation if any (286).
		if(targetItem.isAugmented())
		{
			// get the price
			long price = VariationUtils.getRemovePrice(targetItem);

			if(price < 0)
			{
				player.sendPacket(new ExVariationCancelResult(0));
				return;
			}

			// try to reduce the players adena
			if(!player.reduceAdena(price, true))
			{
				player.sendPacket(new ExVariationCancelResult(0), SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}

			VariationUtils.setVariation(player, targetItem, 0, 0, 0, 0);
		}

		VariationUtils.setVariation(player, targetItem, stoneId, option1Id, option2Id, option3Id);
		player.getListeners().onRefineItem(targetItem);

		player.sendPacket(new ExApplyVariationOption(1, _enchantedObjectId, _option1, _option2, _option3));
		request.cancel();
	}

}