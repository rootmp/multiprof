package l2s.gameserver.model.items.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.stats.Formulas;
import l2s.gameserver.templates.agathion.AgathionData;
import l2s.gameserver.templates.agathion.AgathionEnchantData;
import l2s.gameserver.templates.item.EtcItemTemplate.EtcItemType;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.WeaponTemplate.WeaponType;
import l2s.gameserver.templates.item.support.Ensoul;

public final class ItemSkillsListener extends AbstractSkillListener
{
	private static final ItemSkillsListener _instance = new ItemSkillsListener();

	private static final SkillEntry SWORD_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50392, 1);
	private static final SkillEntry BIG_SWORD_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50395, 1);
	private static final SkillEntry BLUNT_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50397, 1);
	private static final SkillEntry BIG_BLUNT_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50399, 1);
	private static final SkillEntry BOW_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50401, 1);
	private static final SkillEntry DAGGER_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50402, 1);
	private static final SkillEntry DUAL_FIST_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50403, 1);
	private static final SkillEntry POLE_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50405, 1);
	private static final SkillEntry FIREARMS_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50497, 1);
	private static final SkillEntry A_5_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50406, 1);
	private static final SkillEntry A_6_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50407, 1);
	private static final SkillEntry A_7_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50408, 1);
	private static final SkillEntry A_8_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50409, 1);
	private static final SkillEntry A_9_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50410, 1);
	private static final SkillEntry A_10_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50411, 1);
	private static final SkillEntry BOSS_5_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50412, 1);
	private static final SkillEntry BOSS_6_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50413, 1);
	private static final SkillEntry BOSS_7_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50414, 1);
	private static final SkillEntry BOSS_8_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50415, 1);
	private static final SkillEntry BOSS_9_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50416, 1);
	private static final SkillEntry BOSS_10_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50417, 1);
	private static final SkillEntry DK_FLAME_SWORD_1_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50418, 1);
	private static final SkillEntry DK_FLAME_SWORD_2_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50419, 1);
	private static final SkillEntry DK_FLAME_SWORD_3_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50420, 1);
	private static final SkillEntry A_16_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50421, 1);
	private static final SkillEntry A_20_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50422, 1);
	private static final SkillEntry A_25_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50423, 1);
	private static final SkillEntry S_16_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50424, 1);
	private static final SkillEntry S_20_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50425, 1);
	private static final SkillEntry S_25_SKILL = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, 50426, 1);

	private static final int[] BOSS_WEAPONS = {
			92421, // Queen Ant's Stone Crasher
			93295, // Queen Ant's Stone Crasher (Sealed)
			92405, // Core's Plasma Bow
			93292, // Core's Plasma Bow (Sealed)
			92404, // Zaken's Blood Sword
			93291, // Zaken's Blood Sword (Sealed)
			92408, // Baium's Thunder Breaker
			93294, // Baium's Thunder Breaker (Sealed)
			92407, // Orfen's Poisonous Sword
			93293, // Orfen's Poisonous Sword (Sealed)
			94888, // Anakim's Divine Shooter
			95422, // Anakim's Divine Shooter (Sealed)
			96268, // Beleth's Soul Eater
			96269, // Beleth's Soul Eater (Sealed)
			95725, // Frost Lord's Sword
			95726, // Frost Lord's Greatsword
			95727, // Frost Lord's Axe
			95728, // Frost Lord's Dagger
			95729, // Frost Lord's Spear
			95730, // Frost Lord's Bow
			95731, // Frost Lord's Blade Fist
			95732, // Frost Lord's Magic Blunt Weapon
			95733, // Frost Lord's Staff
			95734, // Frost Lord's Ancient Sword
			95735, // Frost Lord's Rapier
			95736, // Frost Lord's Gun
			95737, // Frost Lord's Twin Blade
			96751, // Frost Lord's Sword (Sealed)
			96752, // Frost Lord's Greatsword (Sealed)
			96753, // Frost Lord's Axe (Sealed)
			95754, // Frost Lord's Dagger (Sealed)
			96755, // Frost Lord's Spear (Sealed)
			96756, // Frost Lord's Bow (Sealed)
			96757, // Frost Lord's Blade Fist (Sealed)
			96758, // Frost Lord's Magic Blunt Weapon (Sealed)
			96759, // Frost Lord's Staff (Sealed)
			96760, // Frost Lord's Ancient Sword (Sealed)
			96761, // Frost Lord's Rapier (Sealed)
			96762, // Frost Lord's Gun (Sealed)
			96763 // Frost Lord's Twin Blade (Sealed)
	};

	private static final int DEATH_KNIGHT_FIRE_SWORD = 93864;

	public static ItemSkillsListener getInstance()
	{
		return _instance;
	}

	public int onEquip(int slot, ItemInstance item, Playable actor, boolean refresh)
	{
		if(!actor.isPlayer())
		{ return 0; }

		Player player = actor.getPlayer();
		ItemTemplate template = item.getTemplate();

		if(!refresh)
		{
			player.removeTriggers(template);
		}

		int flags = 0;

		List<SkillEntry> addedSkills = new ArrayList<SkillEntry>();

		// Для оружия при несоотвествии грейда скилы не выдаем
		if((template.getType2() != ItemTemplate.TYPE2_WEAPON) || (player.getWeaponsExpertisePenalty() == 0))
		{
			if(!refresh)
			{
				player.addTriggers(template);
			}

			if(template.getItemType() == EtcItemType.RUNE_SELECT)
			{
				for(SkillEntry itemSkillEntry : template.getAttachedSkills())
				{
					int skillsCount = 1;
					for(ItemInstance ii : player.getInventory().getItems())
					{
						if(ii == item)
						{
							continue;
						}

						ItemTemplate it = ii.getTemplate();
						if(it.getItemType() == EtcItemType.RUNE_SELECT)
						{
							for(SkillEntry se : it.getAttachedSkills())
							{
								if(se == itemSkillEntry)
								{
									skillsCount++;
									break;
								}
							}
						}
					}

					int skillLevel = Math.min(itemSkillEntry.getTemplate().getMaxLevel(), skillsCount);
					SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, itemSkillEntry.getId(), skillLevel);
					if(skillEntry != null)
					{
						addedSkills.add(skillEntry);
					}
				}
			}
			else
			{
				addedSkills.addAll(Arrays.asList(template.getAttachedSkills()));

				for(int e = item.getFixedEnchantLevel(player); e >= 0; e--)
				{
					List<SkillEntry> enchantSkills = template.getEnchantSkills(e);
					if(enchantSkills != null)
					{
						addedSkills.addAll(enchantSkills);
						break;
					}
				}

				AgathionData agathionData = item.getTemplate().getAgathionData();
				if(agathionData != null)
				{
					for(int e = item.getFixedEnchantLevel(player); e >= 0; e--)
					{
						AgathionEnchantData agathionItemEnchant = agathionData.getEnchant(e);
						if(agathionItemEnchant != null)
						{
							if(item.getEquipSlot() == Inventory.PAPERDOLL_AGATHION_MAIN)
							{
								addedSkills.addAll(agathionItemEnchant.getMainSkills());
							}
							addedSkills.addAll(agathionItemEnchant.getSubSkills());
							break;
						}
					}
				}

				addedSkills.addAll(item.getAppearanceStoneSkills());

				for(Ensoul ensoul : item.getNormalEnsouls())
				{
					addedSkills.addAll(ensoul.getSkills());
				}

				for(Ensoul ensoul : item.getSpecialEnsouls())
				{
					addedSkills.addAll(ensoul.getSkills());
				}
			}
		}

		addBlessingSkills(item, player);

		flags |= refreshSkills(actor, item, addedSkills);
		return flags;
	}

	@Override
	protected boolean canAddSkill(Playable actor, ItemInstance item, SkillEntry skillEntry)
	{
		if(item.getTemplate().getItemType() == EtcItemType.RUNE_SELECT)
		{ return true; }

		if(skillEntry.getLevel() < actor.getSkillLevel(skillEntry.getId()))
		{
			for(ItemInstance tempItem : actor.getInventory().getItems())
			{
				if(tempItem != item)
				{
					int tempSkillLevel = tempItem.getEquippedSkillLevel(skillEntry.getId());
					if(tempSkillLevel > skillEntry.getLevel())
					{ return false; }
				}
			}
			return true;
		}
		return skillEntry.getLevel() > actor.getSkillLevel(skillEntry.getId());
	}

	@Override
	protected int onAddSkill(Playable actor, ItemInstance item, SkillEntry skillEntry)
	{
		Skill itemSkill = skillEntry.getTemplate();
		if(itemSkill.isActive())
		{
			if(!actor.isSkillDisabled(itemSkill))
			{
				long reuseDelay = Formulas.calcSkillReuseDelay(actor, itemSkill);
				reuseDelay = Math.min(reuseDelay, 30000);

				if(reuseDelay > 0)
				{
					actor.disableSkill(itemSkill, reuseDelay);
				}
			}
		}
		return 0;
	}

	@Override
	public int onEquip(int slot, ItemInstance item, Playable actor)
	{
		return onEquip(slot, item, actor, false);
	}

	@Override
	public int onUnequip(int slot, ItemInstance item, Playable actor)
	{
		if(!actor.isPlayer())
		{ return 0; }

		removeBlessingSkills(actor.getPlayer());
		actor.removeTriggers(item.getTemplate());
		return super.onUnequip(slot, item, actor);
	}

	@Override
	public int onRefreshEquip(ItemInstance item, Playable actor)
	{
		return onEquip(item.getEquipSlot(), item, actor, true);
	}

	private void addBlessingSkills(ItemInstance item, Player player)
	{
		if(item.isWeapon() && item.isBlessed())
		{
			if((item.getItemType() == WeaponType.SWORD) || (item.getItemType() == WeaponType.RAPIER))
			{
				player.addSkill(SWORD_SKILL);
			}
			else if((item.getItemType() == WeaponType.BIGSWORD) || (item.getItemType() == WeaponType.ANCIENTSWORD))
			{
				player.addSkill(BIG_SWORD_SKILL);
			}
			else if(item.getItemType() == WeaponType.BIGBLUNT)
			{
				player.addSkill(BIG_BLUNT_SKILL);
			}
			else if(item.getItemType() == WeaponType.BLUNT)
			{
				player.addSkill(BLUNT_SKILL);
			}
			else if(item.getItemType() == WeaponType.BOW)
			{
				player.addSkill(BOW_SKILL);
			}
			else if(item.getItemType() == WeaponType.DAGGER)
			{
				player.addSkill(DAGGER_SKILL);
			}
			else if((item.getItemType() == WeaponType.DUAL) || (item.getItemType() == WeaponType.DUALFIST))
			{
				player.addSkill(DUAL_FIST_SKILL);
			}
			else if(item.getItemType() == WeaponType.POLE)
			{
				player.addSkill(POLE_SKILL);
			}
			else if(item.getItemType() == WeaponType.FIREARMS)
			{
				player.addSkill(FIREARMS_SKILL);
			}

			if((item.getGrade() == ItemGrade.A) && !ArrayUtils.contains(BOSS_WEAPONS, item.getItemId()))
			{
				if(item.getEnchantLevel() >= 5)
				{
					player.addSkill(A_5_SKILL);
				}
				if(item.getEnchantLevel() >= 6)
				{
					player.addSkill(A_6_SKILL);
				}
				if(item.getEnchantLevel() >= 7)
				{
					player.addSkill(A_7_SKILL);
				}
				if(item.getEnchantLevel() >= 8)
				{
					player.addSkill(A_8_SKILL);
				}
				if(item.getEnchantLevel() >= 9)
				{
					player.addSkill(A_9_SKILL);
				}
				if(item.getEnchantLevel() >= 10)
				{
					player.addSkill(A_10_SKILL);
				}
				if(item.getEnchantLevel() >= 16)
				{
					player.addSkill(A_16_SKILL);
				}
				if(item.getEnchantLevel() >= 20)
				{
					player.addSkill(A_20_SKILL);
				}
				if(item.getEnchantLevel() >= 25)
				{
					player.addSkill(A_25_SKILL);
				}
			}
			if((item.getGrade() == ItemGrade.S) && !ArrayUtils.contains(BOSS_WEAPONS, item.getItemId()))
			{
				if(item.getEnchantLevel() >= 16)
				{
					player.addSkill(S_16_SKILL);
				}
				if(item.getEnchantLevel() >= 20)
				{
					player.addSkill(S_20_SKILL);
				}
				if(item.getEnchantLevel() >= 25)
				{
					player.addSkill(S_25_SKILL);
				}
			}
			// Boss Weapons
			if(ArrayUtils.contains(BOSS_WEAPONS, item.getItemId()))
			{
				if(item.getEnchantLevel() >= 5)
				{
					player.addSkill(BOSS_5_SKILL);
				}
				if(item.getEnchantLevel() >= 6)
				{
					player.addSkill(BOSS_6_SKILL);
				}
				if(item.getEnchantLevel() >= 7)
				{
					player.addSkill(BOSS_7_SKILL);
				}
				if(item.getEnchantLevel() >= 8)
				{
					player.addSkill(BOSS_8_SKILL);
				}
				if(item.getEnchantLevel() >= 9)
				{
					player.addSkill(BOSS_9_SKILL);
				}
				if(item.getEnchantLevel() >= 10)
				{
					player.addSkill(BOSS_10_SKILL);
				}
			}
			// Death Knight Sword
			if(item.getItemId() == DEATH_KNIGHT_FIRE_SWORD)
			{
				if(item.getEnchantLevel() >= 1)
				{
					player.addSkill(DK_FLAME_SWORD_1_SKILL);
				}
				if(item.getEnchantLevel() >= 2)
				{
					player.addSkill(DK_FLAME_SWORD_2_SKILL);
				}
				if(item.getEnchantLevel() >= 3)
				{
					player.addSkill(DK_FLAME_SWORD_3_SKILL);
				}
			}
		}
	}

	private void removeBlessingSkills(Player player)
	{
		player.removeSkill(SWORD_SKILL, false);
		player.removeSkill(BIG_SWORD_SKILL, false);
		player.removeSkill(BIG_BLUNT_SKILL, false);
		player.removeSkill(BLUNT_SKILL, false);
		player.removeSkill(BOW_SKILL, false);
		player.removeSkill(DAGGER_SKILL, false);
		player.removeSkill(DUAL_FIST_SKILL, false);
		player.removeSkill(POLE_SKILL, false);
		player.removeSkill(FIREARMS_SKILL, false);
		player.removeSkill(A_5_SKILL, false);
		player.removeSkill(A_6_SKILL, false);
		player.removeSkill(A_7_SKILL, false);
		player.removeSkill(A_8_SKILL, false);
		player.removeSkill(A_9_SKILL, false);
		player.removeSkill(A_10_SKILL, false);
		player.removeSkill(A_16_SKILL, false);
		player.removeSkill(A_20_SKILL, false);
		player.removeSkill(A_25_SKILL, false);
		player.removeSkill(S_16_SKILL, false);
		player.removeSkill(S_20_SKILL, false);
		player.removeSkill(S_25_SKILL, false);
		player.removeSkill(BOSS_5_SKILL, false);
		player.removeSkill(BOSS_6_SKILL, false);
		player.removeSkill(BOSS_7_SKILL, false);
		player.removeSkill(BOSS_8_SKILL, false);
		player.removeSkill(BOSS_9_SKILL, false);
		player.removeSkill(BOSS_10_SKILL, false);
		player.removeSkill(DK_FLAME_SWORD_1_SKILL, false);
		player.removeSkill(DK_FLAME_SWORD_2_SKILL, false);
		player.removeSkill(DK_FLAME_SWORD_3_SKILL, false);
	}
}