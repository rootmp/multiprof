package l2s.gameserver.handler.items.impl;

import l2s.gameserver.ai.PlayableAI.AINextAction;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.SoulShotType;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author VISTALL
 * @date 7:27/17.03.2011
 */
public class EquipableItemHandler extends DefaultItemHandler
{
	private static final long CP_CS_MERCENARIES = 4194304;

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if (playable.isPet())
		{
			PetInstance pet = (PetInstance) playable;
			IBroadcastPacket sm = ItemFunctions.checkIfCanEquip(pet, item);
			if (sm == null)
			{
				if (item.isEquipped())
				{
					pet.getInventory().unEquipItem(item);
				}
				else
				{
					pet.getInventory().equipItem(item);
				}
				pet.broadcastCharInfo();
				return true;
			}

			if (pet.getPlayer() != null)
			{
				pet.getPlayer().sendPacket(sm);
			}
			return false;
		}

		if (!playable.isPlayer())
		{
			return false;
		}
		Player player = playable.getPlayer();
		// You can not take off / put on any equipment under these conditions
		if (player.isStunned() || player.isSleeping() || player.isDecontrolled() || player.isAlikeDead() || player.isWeaponEquipBlocked())
		{
			player.sendPacket(new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}

		long bodyPart = item.getBodyPart();
		if (bodyPart == ItemTemplate.SLOT_LR_HAND || bodyPart == ItemTemplate.SLOT_L_HAND || bodyPart == ItemTemplate.SLOT_R_HAND)
		{
			// You can't take off/put on weapons while sitting on a pet
			// Can't take off/put on cursed weapons and flags
			// You can't equip/unequip a weapon/shield/sigil while driving a ship
			if (player.isMounted() || player.getActiveWeaponFlagAttachment() != null)
			{
				player.sendPacket(new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
				return false;
			}
		}

		if (player.isInFightClub() && player.getActiveWeaponInstance() != null && player.getActiveWeaponInstance().getItemId() == 38437)
		{
			player.sendMessage("You cannot unequip the flag!");
			return false;
		}

		if (player.isAttackingNow() || player.isCastingNow())
		{
			player.getAI().setNextAction(AINextAction.EQUIP, item, null, ctrl, true);
			player.sendActionFailed();
			return false;
		}

		if (item.isEquipped())
		{
			ItemInstance weapon = player.getActiveWeaponInstance();
			if (player.isInFightClub() && item.getItemId() == 38437)
			{
				player.sendMessage("You cannot unequip the flag!");
				return false;
			}
			if (item == weapon)
			{
				player.abortAttack(true, true);
				player.abortCast(true, true);
				player.removeAutoShot(SoulShotType.SOULSHOT);
				player.removeAutoShot(SoulShotType.SPIRITSHOT);
			}

			player.sendDisarmMessage(item);
			player.getInventory().unEquipItem(item);
			player.refreshInventory();
			return false;
		}

		if (bodyPart == CP_CS_MERCENARIES)
		{
			for (int paperdoll = Inventory.PAPERDOLL_DECO1; paperdoll <= Inventory.PAPERDOLL_DECO6; paperdoll++)
			{
				ItemInstance deco = player.getInventory().getPaperdollItem(paperdoll);
				if (deco != null)
				{
					if (deco.getItemId() == item.getItemId())
					{
						player.sendDisarmMessage(deco);
						player.getInventory().unEquipItem(deco);
						player.refreshInventory();
					}
				}
			}
		}

		IBroadcastPacket broadcast = ItemFunctions.checkIfCanEquip(player, item);
		if (broadcast != null)
		{
			player.sendPacket(broadcast);
			{
				return false;
			}
		}

		player.getInventory().equipItem(item);
		if (!item.isEquipped())
		{
			player.sendActionFailed();
			return false;
		}

		SystemMessage sm;
		if (item.getFixedEnchantLevel(player) > 0)
		{
			sm = new SystemMessage(SystemMessage.EQUIPPED__S1_S2);
			sm.addNumber(item.getFixedEnchantLevel(player));
			sm.addItemName(item.getItemId());
		}
		else
		{
			sm = new SystemMessage(SystemMessage.YOU_HAVE_EQUIPPED_YOUR_S1).addItemName(item.getItemId());
		}

		player.sendPacket(sm);
		player.refreshInventory();
		return true;
	}
}
