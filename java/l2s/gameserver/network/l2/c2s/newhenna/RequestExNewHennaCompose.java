package l2s.gameserver.network.l2.c2s.newhenna;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.dataparser.data.holder.DyeDataHolder;
import l2s.dataparser.data.holder.SynthesisHolder;
import l2s.dataparser.data.holder.synthesis.SynthesisData;
import l2s.dataparser.data.holder.synthesis.SynthesisData.ItemResult;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExEnchantRetryToPutItemFail;
import l2s.gameserver.network.l2.s2c.newhenna.NewHennaPotenCompose;
import l2s.gameserver.templates.item.henna.Henna;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExNewHennaCompose implements IClientIncomingPacket
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestExNewHennaCompose.class);
	
	private int _slotOneIndex;
	@SuppressWarnings("unused")
	private int _slotOneItemId;
	private int _slotTwoItemId;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotOneIndex = packet.readD();
		_slotOneItemId = packet.readD();
		_slotTwoItemId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;
		
		Henna henna = player.getHenna(_slotOneIndex);
		if(henna==null)
			return;

		ItemInstance item2 = player.getInventory().getItemByObjectId(_slotTwoItemId);
		if(item2 == null)
			return;
		
		SynthesisData data = null;
		for(SynthesisData d : SynthesisHolder.getInstance().getDatas())
		{
			if(henna.getDyeItemId() == d.getItem1Id() && item2.getItemId() == d.getItem2Id())
			{
				data = d;
				break;
			}
		}
		if(data == null)
			return;
		
		
		if(Math.round(data.getPrice()/2) > 0&&!player.reduceAdena(Math.round(data.getPrice()/2), true))
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		
		if(data.getSuccessItemData().getChance() < 0)
		{
			player.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			LOGGER.warn("Chance in synthesis data ID1[" + data.getItem1Id() + "] ID2[" + data.getItem2Id() + "] not specified!");
			return;
		}
		
		final Inventory inventory = player.getInventory();

		inventory.writeLock();
		try
		{
			if(ItemFunctions.deleteItem(player, item2, 1, true))
			{
				player.removeHenna(_slotOneIndex);
				double chance = data.getSuccessItemData().getChance();
				if(Rnd.chance(chance))
				{
					ItemResult succeItemData = data.getSuccessItemData();
					final Henna henna_new = DyeDataHolder.getInstance().getHennaByItemId(succeItemData.getId());
					player.addHenna(_slotOneIndex, henna_new, true);
					player.sendPacket(new NewHennaPotenCompose(henna_new.getDyeId(), -1, true));
				}
				else
				{
					ItemResult failItemData = data.getFailItemData();
					final Henna henna_new = DyeDataHolder.getInstance().getHennaByItemId(failItemData.getId());
					player.addHenna(_slotOneIndex, henna_new, true);
					player.sendPacket(new NewHennaPotenCompose(henna_new.getDyeId(), -1, false));
				}
			}
	
			player.setSynthesisItem1(null);
			player.setSynthesisItem2(null);
		}
		finally
		{
			inventory.writeUnlock();
		}
	}
}
