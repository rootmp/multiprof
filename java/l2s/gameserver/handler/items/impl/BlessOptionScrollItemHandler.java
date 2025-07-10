/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.gameserver.handler.items.impl;

import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.blessing.ExOpenBlessOptionScroll;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class BlessOptionScrollItemHandler extends DefaultItemHandler
{
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if (playable == null || !playable.isPlayer() || item == null)
		{
			return false;
		}

		Player player = (Player) playable;
		if (player.getObjectId() != item.getOwnerId())
		{
			return false;
		}

		player.setEnchantScroll(item);
		player.sendPacket(new ExOpenBlessOptionScroll(player, item));
		return true;
	}
}
