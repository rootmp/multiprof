package l2s.gameserver.network.l2.s2c.items.autopeel;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.data.CapsuledItemData;
import l2s.gameserver.templates.item.data.ChancedItemData;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public class ExResultItemAutoPeel implements IClientOutgoingPacket
{
	private final Player _player;
	private final ItemInstance _item;
	private final long _totalPeelCount;
	private final long _remainPeelCount;
	private int _size = 0;
	private final List<ItemInstance> _items = new ArrayList<ItemInstance>();

	public ExResultItemAutoPeel(Player player, ItemInstance item, long totalPeelCount, long remainPeelCount)
	{
		_player = player;
		_item = item;
		_totalPeelCount = totalPeelCount;
		_remainPeelCount = remainPeelCount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		final boolean result = getResultItems(_player, _item);

		packetWriter.writeC(result);
		packetWriter.writeQ(_totalPeelCount);
		packetWriter.writeQ(_remainPeelCount);
		packetWriter.writeD(_size); // TODO Wrong amount if items are isStackable()

		for (int i = 0; i < _size; i++)
		{
			packetWriter.writeD(_items.get(i).getItemId());
			packetWriter.writeQ(_items.get(i).getCount());
			packetWriter.writeD(0); // TODO Announce level
		}
		return true;
	}

	/**
	 * @param player
	 * @param item
	 * @return
	 */
	private boolean getResultItems(Player player, ItemInstance item)
	{
		_items.clear();
		if (!canBeExtracted(player, item))
		{
			return false;
		}

		if (!reduceItem(player, item))
		{
			return false;
		}

		List<CapsuledItemData> capsuled_items = item.getTemplate().getCapsuledItems();
		for (CapsuledItemData ci : capsuled_items)
		{
			if (Rnd.chance(ci.getChance()))
			{
				long count;
				long minCount = ci.getMinCount();
				long maxCount = ci.getMaxCount();
				int enchantLevel = ci.getEnchantLevel();
				if (minCount == maxCount)
				{
					count = minCount;
				}
				else
				{
					count = Rnd.get(minCount, maxCount);
				}

				ItemTemplate t = ItemHolder.getInstance().getTemplate(ci.getId());

				if (t.isStackable())
				{
					ItemInstance itm = ItemFunctions.createItem(ci.getId());
					itm.setCount(count);
					_items.add(player.getInventory().addItem(ci.getId(), count));
					player.sendPacket(SystemMessagePacket.obtainItems(ci.getId(), count, 0));
				}
				else
				{
					for (long i = 0; i < count; i++)
					{
						_size++;
						ItemInstance itm = player.getInventory().addItem(ci.getId(), 1, enchantLevel);
						itm.setCount(count);
						_items.add(itm);
						player.sendPacket(SystemMessagePacket.obtainItems(itm));
					}
				}
			}
		}

		ChancedItemData ci = item.getTemplate().getCreateItems().chance();
		if (ci != null)
		{
			ItemTemplate t = ItemHolder.getInstance().getTemplate(ci.getId());
			if (t.isStackable())
			{
				_items.add(player.getInventory().addItem(ci.getId(), ci.getCount()));
				player.sendPacket(SystemMessagePacket.obtainItems(ci.getId(), ci.getEnchant(), 0));
			}
			else
			{
				for (long i = 0; i < ci.getCount(); i++)
				{
					_size++;
					ItemInstance itm = player.getInventory().addItem(ci.getId(), ci.getCount(), ci.getEnchant());
					_items.add(itm);
					player.sendPacket(SystemMessagePacket.obtainItems(itm));
				}
			}
		}

		player.sendPacket(SystemMessagePacket.removeItems(item.getItemId(), 1));
		return true;
	}

	public static boolean canBeExtracted(Player player, ItemInstance item)
	{
		if ((player.getWeightPenalty() >= 3) || (player.getInventory().getSize() > (player.getInventoryLimit() - 10)))
		{
			player.sendPacket(SystemMsg.YOUR_INVENTORY_IS_FULL, new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}
		return true;
	}

	public static boolean reduceItem(Playable playable, ItemInstance item)
	{
		if (playable.getInventory().destroyItem(item, 1))
		{
			return true;
		}
		return false;
	}
}