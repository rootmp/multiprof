package l2s.gameserver.handler.items.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Config;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author VISTALL
 * @date 7:34/17.03.2011
 */
public class SkillsItemHandler extends DefaultItemHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SkillsItemHandler.class);

	@Override
	public void attachSkill(ItemTemplate itemTemplate, Skill skill)
	{
		boolean haveNoAltSkill = false;
		for (SkillEntry skillEntry : itemTemplate.getAttachedSkills())
		{
			if (!skillEntry.isAltUse())
			{
				haveNoAltSkill = true;
				break;
			}
		}

		if (!skill.isAltUse(SkillEntryType.CUNSUMABLE_ITEM))
		{
			if (haveNoAltSkill)
				LOGGER.warn(String.format("Item ID[%d] already has a \"no-alt\" skill: ID[%d] LEVEL[%d] that can lead to malfunction of item!", itemTemplate.getItemId(), skill.getId(), skill.getLevel()));
		}
		itemTemplate.addAttachedSkill(SkillEntry.makeSkillEntry(SkillEntryType.CUNSUMABLE_ITEM, skill));
	}

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if (!playable.isPlayer() && !playable.isPet())
			return false;

		// TODO: [Bonux] Тупая заглушка...
		if (playable.isPet())
		{
			PetInstance pet = (PetInstance) playable;
			if (!pet.isMyFeed(item.getItemId()) && !ArrayUtils.contains(Config.ALT_ALLOWED_PET_POTIONS, item.getItemId()))
			{
				// TODO: Вынести все в другое правильное место.
				if (pet.getPlayer() != null)
					pet.getPlayer().sendPacket(SystemMsg.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
				return false;
			}
		}

		boolean sendMessage = false;

		SkillEntry[] skills = item.getTemplate().getAttachedSkills();
		for (int i = 0; i < skills.length; i++)
		{
			SkillEntry skillEntry = skills[i];
			Creature aimingTarget = skillEntry.getTemplate().getAimingTarget(playable, playable.getTarget());
			if (skillEntry.checkCondition(playable, aimingTarget, ctrl, false, true))
			{
				if (!playable.getAI().Cast(skillEntry, aimingTarget, ctrl, false))
					return false;

				if (!skillEntry.isAltUse())
					sendMessage = true;
			}
			else if (i == 0) // FIXME [VISTALL] всегда первый скил идет вместо конда?
				return false;
		}

		if (reduceAfterUse())
			ItemFunctions.deleteItem(playable, item, 1, sendMessage);

		return true;
	}

	public boolean reduceAfterUse()
	{
		return false;
	}
}
