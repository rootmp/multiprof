package l2s.gameserver.model.items;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.ItemLocation;

public class PcPenalty extends Warehouse
{
	public PcPenalty(Player owner)
	{
		super(owner.getObjectId());
	}

	public PcPenalty(int ownerId)
	{
		super(ownerId);
	}

	@Override
	public ItemLocation getItemLocation()
	{
		return ItemLocation.PENALTY_INVENTORY;
	}
}