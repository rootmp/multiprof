package l2s.gameserver.model.actor.instances.player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import l2s.gameserver.dao.EnchantBrokenItemsDAO;
import l2s.gameserver.data.xml.holder.BlackCouponHolder;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.EnchantBrokenItem;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.itemrestore.ExItemRestore;
import l2s.gameserver.network.l2.s2c.itemrestore.ExItemRestoreList;
import l2s.gameserver.templates.BlackCoupon;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class EnchantBrokenItemList
{
	private final Player owner;
	private final List<EnchantBrokenItem> items = new ArrayList<>();

	private int lastItemId = 0;

	public EnchantBrokenItemList(Player owner)
	{
		this.owner = owner;
	}

	public void restore()
	{
		EnchantBrokenItemsDAO.getInstance().select(owner.getObjectId(), items);
	}

	public void add(ItemInstance item)
	{
		EnchantBrokenItem brokenItem = new EnchantBrokenItem(item.getItemId(), item.getEnchantLevel(), (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
		if (EnchantBrokenItemsDAO.getInstance().insert(owner.getObjectId(), brokenItem))
		{
			items.add(brokenItem);
		}
	}

	public boolean showRestoreWindowCategory(int category)
	{
		int itemId = lastItemId;
		if (itemId <= 0)
			return false;

		BlackCoupon blackCoupon = BlackCouponHolder.getInstance().getBlackCoupon(itemId);
		if (blackCoupon == null)
			return false;

		List<ExItemRestoreList.PkItemRestoreNode> nodes = new ArrayList<>();
		for (EnchantBrokenItem item : items)
		{
			int fixedId = blackCoupon.getFixedId(item);
			if (fixedId <= 0)
				continue;

			ItemTemplate template = ItemHolder.getInstance().getTemplate(item.getId());
			if (template == null)
				continue;

			if (category == 0)
			{ // Weapons
				if (!template.isWeapon())
					continue;
			}
			else if (category == 1)
			{ // Armors
				if (!template.isArmor())
					continue;
			}
			else if (category == 2)
			{ // Boss Accessories
				if (!template.isAccessory())
					continue;
				if (template.getGrade() != ItemGrade.NONE)
					continue;
			}
			else if (category == 3)
			{ // Other
				if (template.isWeapon())
					continue;
				if (template.isArmor())
					continue;
				if (template.isAccessory() && template.getGrade() == ItemGrade.NONE)
					continue;
			}

			nodes.add(new ExItemRestoreList.PkItemRestoreNode(item.getId(), fixedId, item.getEnchant(), 1));
		}
		owner.sendPacket(new ExItemRestoreList(itemId, nodes));
		return true;
	}

	public boolean showRestoreWindow(int itemId)
	{
		lastItemId = itemId;
		return showRestoreWindowCategory(0);
	}

	public synchronized void restoreItem(int brokenItemId, int enchant)
	{
		int itemId = lastItemId;
		if (itemId <= 0)
		{
			owner.sendPacket(ExItemRestore.FAIL);
			return;
		}

		BlackCoupon blackCoupon = BlackCouponHolder.getInstance().getBlackCoupon(itemId);
		if (blackCoupon == null)
		{
			owner.sendPacket(ExItemRestore.FAIL);
			return;
		}

		EnchantBrokenItem brokenItem = null;
		for (EnchantBrokenItem i : items)
		{
			if (i.getId() != brokenItemId)
				continue;

			if (i.getEnchant() != enchant)
				continue;

			int fixedId = blackCoupon.getFixedId(i);
			if (fixedId <= 0)
				continue;

			brokenItem = i;
			break;
		}

		if (brokenItem == null || !items.remove(brokenItem))
		{
			owner.sendPacket(ExItemRestore.FAIL);
			return;
		}

		owner.getInventory().writeLock();
		try
		{
			if (!ItemFunctions.haveItem(owner, itemId, 1))
			{
				owner.sendPacket(ExItemRestore.FAIL);
				return;
			}
			if (!EnchantBrokenItemsDAO.getInstance().delete(owner.getObjectId(), brokenItem))
			{
				owner.sendPacket(ExItemRestore.FAIL);
				return;
			}
			ItemFunctions.deleteItem(owner, itemId, 1, true);
		}
		finally
		{
			owner.getInventory().writeUnlock();
		}

		ItemFunctions.addItem(owner, brokenItem.getId(), 1, brokenItem.getEnchant(), true);
		owner.sendPacket(ExItemRestore.SUCCESS);
	}
}
