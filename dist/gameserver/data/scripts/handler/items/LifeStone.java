package handler.items;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExPutIntensiveResultForVariationMake;
import l2s.gameserver.network.l2.s2c.ExShowVariationMakeWindow;

/**
 * @author nexvill
 */
public class LifeStone extends SimpleItemHandler
{
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		int itemId = item.getItemId();

		switch (itemId)
		{
			case 94185: // Life Stone Lv. 1 - Weapon
			case 94186: // Life Stone Lv. 2 - Weapon
			case 94187: // Life Stone Lv. 1 - Armor
			case 94188: // Life Stone Lv. 2 - Armor
			case 94209: // Life Stone Lv. 1 - Weapon (Event)
			case 94210: // Life Stone Lv. 2 - Weapon (Event)
			case 94211: // Life Stone Lv. 1 - Armor (Event)
			case 94212: // Life Stone Lv. 2 - Armor (Event)
			case 94303: // Life Stone - Heroic Circlet
			case 94304: // Life Stone - Heroic Circlet (Event)
			case 94423: // Life Stone - Accessory
			case 94424: // Life Stone - Accessory (Event)
				player.sendPacket(SystemMsg.SELECT_THE_ITEM_TO_BE_AUGMENTED, ExShowVariationMakeWindow.STATIC);
				player.sendPacket(new ExPutIntensiveResultForVariationMake(item.getObjectId(), itemId, 0, 0));
				break;
			default:
				return false;
		}

		return true;
	}
}
