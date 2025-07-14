package l2s.gameserver.handler.items.impl;

import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ExItemAnnounce;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.item.data.CapsuledItemData;
import l2s.gameserver.templates.item.data.ChancedItemData;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author Bonux, Hl4p3x
 */
public class CapsuledItemHandler extends DefaultItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		Player player;
		final int itemId = item.getItemId();

		if(playable.isPlayer())
		{
			player = (Player) playable;
		}
		else if(playable.isPet())
		{
			player = playable.getPlayer();
		}
		else
		{
			return false;
		}

		if(!canBeExtracted(player, item))
		{ return false; }
		if(!reduceItem(player, item))
		{ return false; }

		final List<CapsuledItemData> capsuled_items = item.getTemplate().getCapsuledItems();
		for(CapsuledItemData ci : capsuled_items)
		{
			if(Rnd.chance(ci.getChance()))
			{
				final long count;
				final long minCount = ci.getMinCount();
				final long maxCount = ci.getMaxCount();
				final int enchantLevel = ci.getEnchantLevel();
				if(minCount == maxCount)
				{
					count = minCount;
				}
				else
				{
					count = Rnd.get(minCount, maxCount);
				}

				// Fix for duplication reward
				final List<ItemInstance> items = ItemFunctions.addItem(player, ci.getId(), count, enchantLevel, true);
				if(ci.isAnnounce())
				{
					for(Player playerr : GameObjectsStorage.getPlayers(false, false))
					{
						playerr.sendPacket(new ExItemAnnounce(player.getPlayer(), items.get(0), 1, item.getItemId()));
						playerr.sendMessage(player.getName() + " opened " + item.getName() + " and obtained: " + (items.get(0)).getName()
								+ ((ci.getEnchantLevel() > 0) ? (" + (" + ci.getEnchantLevel() + ") ") : "") + " x" + count);
					}
				}
			}
		}

		ChancedItemData ci = item.getTemplate().getCreateItems().chance();
		if(ci != null)
		{
			ItemFunctions.addItem(player, ci.getId(), ci.getCount(), ci.getEnchant(), true);
		}

		player.sendPacket(SystemMessagePacket.removeItems(itemId, 1));
		return true;
	}
}
