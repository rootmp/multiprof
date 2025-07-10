package l2s.gameserver.model.entity.events.objects;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.entity.events.Event;
import l2s.gameserver.model.entity.events.impl.FortressSiegeEvent;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.attachment.FlagItemAttachment;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class FortressCombatFlagObject implements SpawnableObject, FlagItemAttachment
{
	private static final Logger LOGGER = LoggerFactory.getLogger(FortressCombatFlagObject.class);

	private static final int FLAG_ITEM_ID = 93331;

	private ItemInstance flagItem;
	private final Location location;

	private FortressSiegeEvent event;

	public FortressCombatFlagObject(Location location)
	{
		this.location = location;
	}

	public boolean dropFlag(FortressSiegeEvent event, NpcInstance dropper, Location loc, Reflection reflection)
	{
		if (flagItem != null)
		{
			return false;
		}
		flagItem = ItemFunctions.createItem(FLAG_ITEM_ID);
		Objects.requireNonNull(flagItem);
		flagItem.setAttachment(this);
		flagItem.dropMe(dropper, loc);
		flagItem.setDropTime(0);

		this.event = event;
		return true;
	}

	@Override
	public void spawnObject(Event event, Reflection reflection)
	{
		if (!(event instanceof FortressSiegeEvent))
		{
			LOGGER.info("FortressCombatFlagObject: can't spawn in not FortressSiegeEvent!");
			return;
		}
		if (!dropFlag((FortressSiegeEvent) event, null, location, reflection))
		{
			LOGGER.info("FortressCombatFlagObject: can't spawn twice: " + event);
		}
	}

	@Override
	public void despawnObject(Event event, Reflection reflection)
	{
		if (flagItem == null)
		{
			return;
		}

		Player owner = GameObjectsStorage.getPlayer(flagItem.getOwnerId());
		if (owner != null)
		{
			owner.getInventory().destroyItem(flagItem);
			owner.sendDisarmMessage(flagItem);
		}

		flagItem.setAttachment(null);
		flagItem.deleteMe();
		flagItem = null;

		this.event = null;
	}

	@Override
	public void respawnObject(Event event, Reflection reflection)
	{
		//
	}

	@Override
	public void refreshObject(Event event, Reflection reflection)
	{
		//
	}

	@Override
	public void onLogout(Player player)
	{
		onLeaveSiegeZone(player);
	}

	@Override
	public void onLeaveSiegeZone(Player player)
	{
		player.getInventory().removeItem(flagItem);

		flagItem.setLocation(ItemLocation.VOID);
		flagItem.dropMe(null, location);
		flagItem.setDropTime(0);
	}

	@Override
	public void onDeath(Player owner, Creature killer)
	{
		onLeaveSiegeZone(owner);
		owner.sendPacket(new SystemMessagePacket(SystemMsg.YOU_HAVE_DROPPED_S1).addItemName(flagItem.getItemId()));
	}

	@Override
	public boolean canPickUp(Player player)
	{
		if ((player.getActiveWeaponFlagAttachment() != null) || player.isMounted())
		{
			return false;
		}
		if (!player.containsEvent(getEvent()))
		{
			return false;
		}
		Clan clan = player.getClan();
		if ((clan == null) || (clan.getLevel() < 5))
		{
			player.sendPacket(SystemMsg.A_FLAG_CAN_BE_CAPTURED_ONLY_BY_A_CLAN_MEMBER_OF_LV_5_OR_HIGHER);
			return false;
		}
		if (clan.getHasFortress() > 0)
		{
			player.sendPacket(SystemMsg.A_CLAN_THAT_OWNS_A_CASTLE_CANNOT_GET_A_FLAG_);
			return false;
		}
		return true;
	}

	@Override
	public void pickUp(Player player)
	{
		player.getInventory().equipItem(flagItem);
		getEvent().broadcastInZone(new SystemMessagePacket(SystemMsg.S1_HAS_CAPTURED_THE_FLAG).addName(player));
	}

	@Override
	public boolean canAttack(Player player)
	{
		player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_PERFORM_ANY_ATTACKS);
		return false;
	}

	@Override
	public boolean canCast(Player player, Skill skill)
	{
		ItemTemplate weaponTemplate = player.getActiveWeaponTemplate();
		if (weaponTemplate != null)
		{
			for (SkillEntry skillEntry : weaponTemplate.getAttachedSkills())
			{
				if (skillEntry.getTemplate().equals(skill))
				{
					return true;
				}
			}
		}
		player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_USE_ANY_OTHER_SKILL_EXCEPT_THE_WEAPONS_SKILL);
		return false;
	}

	@Override
	public boolean canBeLost()
	{
		return false;
	}

	@Override
	public boolean canBeUnEquiped()
	{
		return false;
	}

	@Override
	public void setItem(ItemInstance item)
	{
		// ignored
	}

	public FortressSiegeEvent getEvent()
	{
		return event;
	}
}
