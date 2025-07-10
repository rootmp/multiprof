package handler.items;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ExShowEnsoulWindow;

/**
 * @author nexvill
 */
public class SoulCrystal extends SimpleItemHandler
{
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		int itemId = item.getItemId();

		switch (itemId)
		{
			case 95783: // Aden's Soul Crystal Lv. 1
			case 95784: // Aden's Soul Crystal Lv. 2
			case 95785: // Aden's Soul Crystal Lv. 3
			case 95786: // Aden's Soul Crystal Lv. 4
			case 95787: // Aden's Soul Crystal Lv. 5
			case 95788: // Aden's Soul Crystal Lv. 6
			case 95789: // Aden's Soul Crystal Lv. 7
			case 95790: // Aden's Soul Crystal Lv. 8
			case 95791: // Aden's Soul Crystal Lv. 9
			case 95792: // Aden's Soul Crystal Lv. 10
			case 95793: // Aden's Soul Crystal Lv. 11
			case 95794: // Aden's Soul Crystal Lv. 12
			case 95795: // Aden's Soul Crystal Lv. 13
			case 95796: // Aden's Soul Crystal Lv. 14
			case 95797: // Aden's Soul Crystal Lv. 15
			case 95798: // Aden's Soul Crystal Lv. 16
			case 95799: // Aden's Soul Crystal Lv. 17
			case 95800: // Aden's Soul Crystal Lv. 18
			case 95801: // Aden's Soul Crystal Lv. 19
			case 95802: // Aden's Soul Crystal Lv. 20
			case 95803: // Hardin's Soul Crystal Lv. 1
			case 95804: // Hardin's Soul Crystal Lv. 2
			case 95805: // Hardin's Soul Crystal Lv. 3
			case 95806: // Hardin's Soul Crystal Lv. 4
			case 95807: // Hardin's Soul Crystal Lv. 5
			case 95808: // Hardin's Soul Crystal Lv. 6
			case 95809: // Hardin's Soul Crystal Lv. 7
			case 95810: // Hardin's Soul Crystal Lv. 8
			case 95811: // Hardin's Soul Crystal Lv. 9
			case 95812: // Hardin's Soul Crystal Lv. 10
			case 95813: // Hardin's Soul Crystal Lv. 11
			case 95814: // Hardin's Soul Crystal Lv. 12
			case 95815: // Hardin's Soul Crystal Lv. 13
			case 95816: // Hardin's Soul Crystal Lv. 14
			case 95817: // Hardin's Soul Crystal Lv. 15
			case 95818: // Hardin's Soul Crystal Lv. 16
			case 95189: // Hardin's Soul Crystal Lv. 17
			case 95820: // Hardin's Soul Crystal Lv. 18
			case 95821: // Hardin's Soul Crystal Lv. 19
			case 95822: // Hardin's Soul Crystal Lv. 20
				player.sendPacket(ExShowEnsoulWindow.STATIC);
				break;
			default:
				return false;
		}

		return true;
	}
}
