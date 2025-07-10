package l2s.gameserver.model.items;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.ItemLocation;

/**
 * @author VISTALL
 * @date 20:20/16.05.2011
 */
public class PcFreight extends Warehouse
{
	public PcFreight(Player player)
	{
		super(player.getObjectId());
	}

	public PcFreight(int objectId)
	{
		super(objectId);
	}

	@Override
	public ItemLocation getItemLocation()
	{
		return ItemLocation.FREIGHT;
	}
}
