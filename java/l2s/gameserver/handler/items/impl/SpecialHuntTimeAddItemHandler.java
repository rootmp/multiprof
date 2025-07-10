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

/**
*import l2s.gameserver.data.xml.holder.TimeRestrictFieldHolder;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.timerestrictfield.ExTimeRestrictFieldList;
import l2s.gameserver.network.l2.s2c.timerestrictfield.ExTimeRestrictFieldUserChargeResult;
import l2s.gameserver.templates.TimeRestrictFieldInfo;
import l2s.gameserver.templates.item.EtcItemTemplate;

 @author Hl4p3x for L2Scripts Essence
public class SpecialHuntTimeAddItemHandler extends SkillsItemHandler
{
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if (!playable.isPlayer())
			return super.useItem(playable, item, ctrl);

		Player player = playable.getPlayer();
		EtcItemTemplate etc = (EtcItemTemplate) item.getTemplate();
		int huntId = etc.getSpecialHuntingZoneId();
		int minutes = etc.getSpecialHuntingZoneAddMinutes();

		if (huntId < 0 || minutes <= 0)
			return false;

		int itemId = item.getItemId();
		int reflectionId = ReflectionManager.getInstance().getReflectionIdForFieldId(huntId);
		if (reflectionId != 0)
		{
			final TimeRestrictFieldInfo field = TimeRestrictFieldHolder.getInstance().getFields().get(Integer.valueOf(huntId));
			if (field == null)
			{
				return false;
			}

			String var = PlayerVariables.RESTRICT_FIELD_TIMELEFT + reflectionId;
			int amountToAdd = minutes * 60;
			int maxAmountAdded = field.getRemainTimeMax();
			int remainTime = player.getVarInt(var, field.getRemainTimeBase());
			int remainTimeRefill = player.getVarInt(var + "_refill", field.getRemainTimeMax() - field.getRemainTimeBase());
			if (!player.isGM() && (remainTimeRefill - amountToAdd < 0 || remainTime + amountToAdd > maxAmountAdded))
			{
				player.sendPacket(SystemMsg.YOU_WILL_EXCEED_THE_MAX_AMOUNT_OF_TIME_FOR_THE_HUNTING_ZONE_SO_YOU_CANNOT_ADD_ANY_MORE_TIME);
				return false;
			}
			if (remainTimeRefill > 0)
			{
				boolean isInTimeRestrictField = (player.getReflection().getId() <= -1000);
				player.setVar(var, Integer.valueOf(remainTime + amountToAdd));
				player.setVar(var + "_refill", Integer.valueOf(remainTimeRefill - amountToAdd));
				player.sendPacket(new ExTimeRestrictFieldUserChargeResult(huntId, remainTime + amountToAdd, field.getRemainTimeBase() / 60));
				if (isInTimeRestrictField)
				{
					player.stopTimedHuntingZoneTask(false, false);
					player.startTimeRestrictField();
				}
			}
			else
			{
				return false;
			}
			if (!reduceItem((Playable) player, item))
			{
				player.sendMessage("Couldn't delete item.");
				return false;
			}
			player.sendPacket(new ExTimeRestrictFieldList(player));
		}
		player.sendPacket(SystemMessagePacket.removeItems(itemId, 1L));
		return super.useItem(playable, item, ctrl);
	}
}
 */
