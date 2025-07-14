package l2s.gameserver.network.l2.c2s;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.dataparser.data.holder.SynthesisHolder;
import l2s.dataparser.data.holder.synthesis.SynthesisData;
import l2s.dataparser.data.holder.synthesis.SynthesisData.ItemResult;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExEnchantFail;
import l2s.gameserver.network.l2.s2c.ExEnchantRetryToPutItemFail;
import l2s.gameserver.network.l2.s2c.ExEnchantSucess;
import l2s.gameserver.network.l2.s2c.InventoryUpdatePacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author Bonux
 **/
public class RequestNewEnchantTry implements IClientIncomingPacket
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestNewEnchantTry.class);

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		//
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(activeChar.isBlocked() || activeChar.isAlikeDead())
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantFail.STATIC);
			return;
		}

		if(activeChar.isInStoreMode())
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantFail.STATIC);
			return;
		}

		if(activeChar.isInTrade())
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantFail.STATIC);
			return;
		}

		if(activeChar.isFishing())
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantFail.STATIC);
			return;
		}

		if(activeChar.isInTrainingCamp())
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantFail.STATIC);
			return;
		}

		final ItemInstance item1 = activeChar.getSynthesisItem1();
		if(item1 == null)
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantFail.STATIC);
			return;
		}

		final ItemInstance item2 = activeChar.getSynthesisItem2();
		if(item2 == null)
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantFail.STATIC);
			return;
		}

		if(item1 == item2 && item1.getCount() <= 1)
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantFail.STATIC);
			return;
		}

		SynthesisData data = null;
		for(SynthesisData d : SynthesisHolder.getInstance().getDatas())
		{
			if(item1.getItemId() == d.getItem1Id() && item1.getEnchantLevel() == d.getItem1IdEnchant() && item2.getItemId() == d.getItem2Id() && item2.getEnchantLevel() == d.getItem2IdEnchant())
			{
				data = d;
				break;
			}

			if(item1.getItemId() == d.getItem2Id() && item1.getEnchantLevel() == d.getItem2IdEnchant() && item2.getItemId() == d.getItem1Id() && item2.getEnchantLevel() == d.getItem1IdEnchant())
			{
				data = d;
				break;
			}
		}

		if(data == null)
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			return;
		}
		
		if(data.getPrice() > 0 && !activeChar.reduceAdena(data.getPrice(), true))
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			activeChar.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}

		/*boolean locationAvailable = false;
		for(Integer locationId : data.getLocationIds())
		{
			if(locationId == -1 || locationId == activeChar.getLocationId())
			{
				locationAvailable = true;
				break;
			}
		}

		if(!locationAvailable)
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			return;
		}*/

		if(data.getSuccessItemData().getChance() < 0)
		{
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
			activeChar.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
			LOGGER.warn("Chance in synthesis data ID1[" + data.getItem1Id() + "] ID2[" + data.getItem2Id() + "] not specified!");
			return;
		}

		final Inventory inventory = activeChar.getInventory();

		final InventoryUpdatePacket iupacket = new InventoryUpdatePacket(activeChar);
		
			if(inventory.getItemByObjectId(item1.getObjectId()) == null)
			{
				activeChar.setSynthesisItem1(null);
				activeChar.setSynthesisItem2(null);
				activeChar.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
				return;
			}

			if(inventory.getItemByObjectId(item2.getObjectId()) == null)
			{
				activeChar.setSynthesisItem1(null);
				activeChar.setSynthesisItem2(null);
				activeChar.sendPacket(ExEnchantRetryToPutItemFail.STATIC_PACKET);
				return;
			}
			double chance = data.getSuccessItemData().getChance();
			if(activeChar.isGM())
				activeChar.sendMessage("chance: "+chance);
			if(Rnd.chance(chance))
			{
				ItemResult succeItemData = data.getSuccessItemData();
				List<ItemInstance> items = ItemFunctions.addItem(activeChar, succeItemData.getId(), succeItemData.getCount(), succeItemData.getEnchant(),true);
				activeChar.sendPacket(new ExEnchantSucess(succeItemData.getId()));
				if(items != null && !items.isEmpty())
				{
					iupacket.addModifiedItem(items.get(0));
					activeChar.getListeners().onNewEnchantItem(items.get(0), true);
				}
			}
			else
			{
				ItemResult failItemData = data.getFailItemData();
				if(failItemData.getId() == 0)
					activeChar.sendPacket(new ExEnchantSucess(failItemData.getId(),failItemData.getEnchant()));
				else
				{
					List<ItemInstance> items = ItemFunctions.addItem(activeChar, failItemData.getId(), failItemData.getCount(), failItemData.getEnchant(),true);
					if(items != null && !items.isEmpty())
					{
						activeChar.sendPacket(new ExEnchantSucess(failItemData.getId(),failItemData.getEnchant()));
						iupacket.addModifiedItem(items.get(0));
						activeChar.getListeners().onNewEnchantItem(items.get(0), true);
					}
				}
			}

			if(ItemFunctions.deleteItem(activeChar, item1, 1, false, false))
			{
				if(item1.getCount()>0)
					iupacket.addModifiedItem(item1);
				else
					iupacket.addRemovedItem(item1);
			}
			if(ItemFunctions.deleteItem(activeChar, item2, 1, false, false))
			{
				if(item2.getCount()>0)
					iupacket.addModifiedItem(item2);
				else
					iupacket.addRemovedItem(item2);
			}
			activeChar.sendPacket(iupacket);
			
			activeChar.setSynthesisItem1(null);
			activeChar.setSynthesisItem2(null);
	}
}