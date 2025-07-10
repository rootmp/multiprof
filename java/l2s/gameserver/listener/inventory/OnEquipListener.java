package l2s.gameserver.listener.inventory;

import l2s.commons.listener.Listener;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.items.ItemInstance;

public interface OnEquipListener extends Listener<Playable>
{
	public int onEquip(int slot, ItemInstance item, Playable actor);

	public int onUnequip(int slot, ItemInstance item, Playable actor);

	public int onRefreshEquip(ItemInstance item, Playable actor);
}
