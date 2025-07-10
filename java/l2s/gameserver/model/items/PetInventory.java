package l2s.gameserver.model.items;

import java.util.Collection;

import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.NpcInfo;
import l2s.gameserver.network.l2.s2c.PetInventoryUpdatePacket;
import l2s.gameserver.network.l2.s2c.PetItemListPacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.utils.ItemFunctions;

public class PetInventory extends Inventory
{
	private final PetInstance _actor;

	public PetInventory(PetInstance actor)
	{
		super(actor.getPlayer().getObjectId());
		_actor = actor;
	}

	@Override
	public PetInstance getActor()
	{
		return _actor;
	}

	public Player getOwner()
	{
		return _actor.getPlayer();
	}

	@Override
	protected ItemLocation getBaseLocation()
	{
		return ItemLocation.INVENTORY;
	}

	@Override
	protected ItemLocation getEquipLocation()
	{
		return ItemLocation.PET_PAPERDOLL;
	}

	@Override
	protected boolean onEquip(int slot, ItemInstance item)
	{
		item.setLocation(getEquipLocation());
		item.setLocData(0);
		item.setEquipped(true);
		item.setJdbcState(JdbcEntityState.UPDATED);

		item.onEquip(slot, getActor());

		_wearedMask |= item.getTemplate().getItemMask();

		sendEquipInfo(slot);
		sendModifyItem(item);
		return true;
	}

	@Override
	public void sendEquipInfo(int slot)
	{
		getOwner().sendItemList(false);
		getOwner().sendPacket(new PetItemListPacket(getActor()));
		getOwner().sendPacket(new NpcInfo.PetInfoPacket(getActor(), getOwner()));
	}

	@Override
	protected boolean onReequip(int slot, ItemInstance newItem, ItemInstance oldItem)
	{
		oldItem.setLocation(getBaseLocation());
		oldItem.setLocData(findSlot(oldItem.getTemplate().isQuest()));
		oldItem.setEquipped(false);
		oldItem.setJdbcState(JdbcEntityState.UPDATED);

		oldItem.setChargedSoulshotPower(0);
		oldItem.setChargedSpiritshotPower(0, 0, 0);
		oldItem.setChargedFishshotPower(0);

		oldItem.onUnequip(slot, getActor());

		_wearedMask &= ~oldItem.getTemplate().getItemMask();

		if (checkPaperdollItem(newItem, slot))
		{
			newItem.setLocation(getEquipLocation());
			newItem.setLocData(0);
			newItem.setEquipped(true);
			newItem.setJdbcState(JdbcEntityState.UPDATED);

			newItem.onEquip(slot, getActor());

			_wearedMask |= newItem.getTemplate().getItemMask();

			sendEquipInfo(slot);
			sendModifyItem(newItem, oldItem);
			return true;
		}
		sendEquipInfo(slot);
		sendModifyItem(oldItem);
		return false;
	}

	@Override
	protected void onUnequip(int slot, ItemInstance item)
	{
		item.setLocation(getBaseLocation());
		item.setLocData(findSlot(item.getTemplate().isQuest()));
		item.setEquipped(false);
		item.setJdbcState(JdbcEntityState.UPDATED);

		item.setChargedSoulshotPower(0);
		item.setChargedSpiritshotPower(0, 0, 0);
		item.setChargedFishshotPower(0);

		item.onUnequip(slot, getActor());

		_wearedMask &= ~item.getTemplate().getItemMask();

		sendEquipInfo(slot);
		sendRemoveItem(item);
	}

	@Override
	protected void onRefreshWeight()
	{
		getActor().sendPetInfo();
	}

	@Override
	public void sendAddItem(ItemInstance item)
	{
		getOwner().sendPacket(new PetInventoryUpdatePacket().addNewItem(item));
	}

	@Override
	public void sendModifyItem(ItemInstance... items)
	{
		PetInventoryUpdatePacket piu = new PetInventoryUpdatePacket();
		for (ItemInstance item : items)
		{
			piu.addModifiedItem(item);
		}

		getOwner().sendPacket(piu);
	}

	@Override
	public void sendRemoveItem(ItemInstance item)
	{
		getOwner().sendPacket(new PetInventoryUpdatePacket().addRemovedItem(item));
	}

	@Override
	public void restore()
	{
		final int ownerId = getOwnerId();

		writeLock();
		try
		{
			Collection<ItemInstance> items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getBaseLocation());

			for (ItemInstance item : items)
			{
				_items.add(item);
				onRestoreItem(item);
			}

			items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getEquipLocation());

			for (ItemInstance item : items)
			{
				_items.add(item);
				onRestoreItem(item);
				if (ItemFunctions.checkIfCanEquip(getActor(), item) == null)
				{
					setPaperdollItem(item.getEquipSlot(), item);
				}
			}
		}
		finally
		{
			writeUnlock();
		}

		refreshWeight();
		checkItems();
	}

	@Override
	public void store()
	{
		writeLock();
		try
		{
			_itemsDAO.update(_items);
		}
		finally
		{
			writeUnlock();
		}
	}

	@Override
	protected void onTemporalItemTimeEnd(ItemInstance item)
	{
		getOwner().sendPacket(new SystemMessagePacket(SystemMsg.S1_HAS_EXPIRED).addItemName(item.getItemId()));
	}

	public void validateItems()
	{
		for (ItemInstance item : _paperdoll)
		{
			if ((item != null) && ((ItemFunctions.checkIfCanEquip(getActor(), item) != null) || !item.getTemplate().testCondition(getActor(), item, false)))
			{
				unEquipItem(item);
			}
		}
	}
}