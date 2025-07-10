package l2s.gameserver.model.entity.events.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.entity.events.Event;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.attachment.FlagItemAttachment;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class CTFCombatFlagObject implements SpawnableObject, FlagItemAttachment
{
	private static final Logger _log = LoggerFactory.getLogger(CTFCombatFlagObject.class);

	private ItemInstance _item;

	private Event _event;

	@Override
	public void spawnObject(Event event, Reflection reflection)
	{
		if (_item != null)
		{
			_log.info("FortressCombatFlagObject: can't spawn twice: " + event);
			return;
		}
		_item = ItemFunctions.createItem(9819);
		_item.setAttachment(this);

		_event = event;
	}

	public ItemInstance getItem()
	{
		return _item;
	}

	@Override
	public void despawnObject(Event event, Reflection reflection)
	{
		if (_item == null) {
			return;
		}
		
		Player owner = GameObjectsStorage.getPlayer(_item.getOwnerId());
		if (owner != null)
		{
			owner.getInventory().destroyItem(_item);
			owner.sendDisarmMessage(_item);
		}

		_item.setAttachment(null);
		_item.setJdbcState(JdbcEntityState.UPDATED);
		_item.delete();

		_item.deleteMe();
		_item = null;

		_event = null;
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
		onDeath(player, null);
	}

	@Override
	public void onDeath(Player owner, Creature killer)
	{
		despawnObject(_event, _event.getReflection());
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
					return true;
			}
		}
		player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_USE_ANY_OTHER_SKILL_EXCEPT_THE_WEAPONS_SKILL);
		return false;
	}

	@Override
	public void setItem(ItemInstance item)
	{
		//
	}

	public Event getEvent()
	{
		return _event;
	}

	@Override
	public boolean canPickUp(Player player)
	{
		return false;
	}

	@Override
	public void pickUp(Player player)
	{
		//
	}

	public boolean canBeLost()
	{
		return false;
	}

	public boolean canBeUnEquiped()
	{
		return false;
	}

	@Override
	public void onLeaveSiegeZone(Player player)
	{
		onDeath(player, null);
	}
}
