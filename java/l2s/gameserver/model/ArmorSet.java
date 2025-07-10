package l2s.gameserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;

public final class ArmorSet
{
	private final List<Integer> _chests = new ArrayList<Integer>(1);
	private final List<Integer> _legs = new ArrayList<Integer>(1);
	private final List<Integer> _head = new ArrayList<Integer>(1);
	private final List<Integer> _gloves = new ArrayList<Integer>(1);
	private final List<Integer> _feet = new ArrayList<Integer>(1);
	private final List<Integer> _shield = new ArrayList<Integer>(1);
	private final TIntObjectHashMap<List<SkillEntry>> _skills = new TIntObjectHashMap<List<SkillEntry>>();
	private final List<SkillEntry> _shieldSkills = new ArrayList<SkillEntry>();
	private final List<SkillEntry> _enchant6skills = new ArrayList<SkillEntry>();
	private final List<SkillEntry> _enchant7skills = new ArrayList<SkillEntry>();
	private final List<SkillEntry> _enchant8skills = new ArrayList<SkillEntry>();
	private final List<SkillEntry> _enchant9skills = new ArrayList<SkillEntry>();
	private final List<SkillEntry> _enchant10skills = new ArrayList<SkillEntry>();

	public ArmorSet(String[] chests, String[] legs, String[] head, String[] gloves, String[] feet, String[] shield, String[] shield_skills, String[] enchant6skills, String[] enchant7skills, String[] enchant8skills, String[] enchant9skills, String[] enchant10skills)
	{
		if (chests != null)
		{
			for (String chestId : chests)
			{
				_chests.add(Integer.parseInt(chestId));
			}
		}

		if (legs != null)
		{
			for (String legsId : legs)
			{
				_legs.add(Integer.parseInt(legsId));
			}
		}

		if (head != null)
		{
			for (String headId : head)
			{
				_head.add(Integer.parseInt(headId));
			}
		}

		if (gloves != null)
		{
			for (String glovesId : gloves)
			{
				_gloves.add(Integer.parseInt(glovesId));
			}
		}

		if (feet != null)
		{
			for (String feetId : feet)
			{
				_feet.add(Integer.parseInt(feetId));
			}
		}

		if (shield != null)
		{
			for (String shieldId : shield)
			{
				_shield.add(Integer.parseInt(shieldId));
			}
		}

		if (shield_skills != null)
		{
			for (String skill : shield_skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					_shieldSkills.add(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLvl));
				}
			}
		}

		if (enchant6skills != null)
		{
			for (String skill : enchant6skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					_enchant6skills.add(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLvl));
				}
			}
		}

		if (enchant7skills != null)
		{
			for (String skill : enchant7skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					_enchant7skills.add(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLvl));
				}
			}
		}

		if (enchant8skills != null)
		{
			for (String skill : enchant8skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					_enchant8skills.add(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLvl));
				}
			}
		}

		if (enchant9skills != null)
		{
			for (String skill : enchant9skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					_enchant9skills.add(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLvl));
				}
			}
		}

		if (enchant10skills != null)
		{
			for (String skill : enchant10skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					_enchant10skills.add(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLvl));
				}
			}
		}
	}

	public void addSkills(int partsCount, String[] skills)
	{
		List<SkillEntry> skillList = new ArrayList<SkillEntry>();
		if (skills != null)
		{
			for (String skill : skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					skillList.add(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLvl));
				}
			}
		}
		_skills.put(partsCount, skillList);
	}

	/**
	 * Checks if player have equipped all items from set (not checking shield)
	 * 
	 * @param player whose inventory is being checked
	 * @return True if player equips whole set
	 */
	public boolean containAll(Player player)
	{
		Inventory inv = player.getInventory();

		ItemInstance chestItem = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		ItemInstance legsItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		ItemInstance headItem = inv.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
		ItemInstance glovesItem = inv.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		ItemInstance feetItem = inv.getPaperdollItem(Inventory.PAPERDOLL_FEET);

		int chest = 0;
		int legs = 0;
		int head = 0;
		int gloves = 0;
		int feet = 0;

		if (chestItem != null)
		{
			chest = chestItem.getItemId();
		}
		if (legsItem != null)
		{
			legs = legsItem.getItemId();
		}
		if (headItem != null)
		{
			head = headItem.getItemId();
		}
		if (glovesItem != null)
		{
			gloves = glovesItem.getItemId();
		}
		if (feetItem != null)
		{
			feet = feetItem.getItemId();
		}

		return containAll(chest, legs, head, gloves, feet);

	}

	public boolean containAll(int chest, int legs, int head, int gloves, int feet)
	{
		if (!_chests.isEmpty() && !_chests.contains(chest))
		{
			return false;
		}
		if (!_legs.isEmpty() && !_legs.contains(legs))
		{
			return false;
		}
		if (!_head.isEmpty() && !_head.contains(head))
		{
			return false;
		}
		if (!_gloves.isEmpty() && !_gloves.contains(gloves))
		{
			return false;
		}
		if (!_feet.isEmpty() && !_feet.contains(feet))
		{
			return false;
		}

		return true;
	}

	public boolean containItem(int slot, int itemId)
	{
		switch (slot)
		{
			case Inventory.PAPERDOLL_CHEST:
				return _chests.contains(itemId);
			case Inventory.PAPERDOLL_LEGS:
				return _legs.contains(itemId);
			case Inventory.PAPERDOLL_HEAD:
				return _head.contains(itemId);
			case Inventory.PAPERDOLL_GLOVES:
				return _gloves.contains(itemId);
			case Inventory.PAPERDOLL_FEET:
				return _feet.contains(itemId);
			default:
				return false;
		}
	}

	public int getEquipedSetPartsCount(Player player)
	{
		Inventory inv = player.getInventory();

		ItemInstance chestItem = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		ItemInstance legsItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		ItemInstance headItem = inv.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
		ItemInstance glovesItem = inv.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		ItemInstance feetItem = inv.getPaperdollItem(Inventory.PAPERDOLL_FEET);

		int chest = 0;
		int legs = 0;
		int head = 0;
		int gloves = 0;
		int feet = 0;

		if (chestItem != null)
		{
			chest = chestItem.getItemId();
		}
		if (legsItem != null)
		{
			legs = legsItem.getItemId();
		}
		if (headItem != null)
		{
			head = headItem.getItemId();
		}
		if (glovesItem != null)
		{
			gloves = glovesItem.getItemId();
		}
		if (feetItem != null)
		{
			feet = feetItem.getItemId();
		}

		int result = 0;
		if (!_chests.isEmpty() && _chests.contains(chest))
		{
			result++;
		}
		if (!_legs.isEmpty() && _legs.contains(legs))
		{
			result++;
		}
		if (!_head.isEmpty() && _head.contains(head))
		{
			result++;
		}
		if (!_gloves.isEmpty() && _gloves.contains(gloves))
		{
			result++;
		}
		if (!_feet.isEmpty() && _feet.contains(feet))
		{
			result++;
		}

		return result;
	}

	public List<SkillEntry> getSkills(int partsCount)
	{
		if (_skills.get(partsCount) == null)
		{
			return new ArrayList<SkillEntry>();
		}

		return _skills.get(partsCount);
	}

	public List<SkillEntry> getSkillsToRemove()
	{
		List<SkillEntry> result = new ArrayList<SkillEntry>();
		for (int i : _skills.keys())
		{
			List<SkillEntry> skills = _skills.get(i);
			if (skills != null)
			{
				for (SkillEntry skill : skills)
				{
					result.add(skill);
				}
			}
		}
		return result;
	}

	public List<SkillEntry> getShieldSkills()
	{
		return _shieldSkills;
	}

	public List<SkillEntry> getEnchant6skills()
	{
		return _enchant6skills;
	}

	public List<SkillEntry> getEnchant7skills()
	{
		return _enchant7skills;
	}

	public List<SkillEntry> getEnchant8skills()
	{
		return _enchant8skills;
	}

	public List<SkillEntry> getEnchant9skills()
	{
		return _enchant9skills;
	}

	public List<SkillEntry> getEnchant10skills()
	{
		return _enchant10skills;
	}

	public boolean containShield(Player player)
	{
		Inventory inv = player.getInventory();

		ItemInstance shieldItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if ((shieldItem != null) && _shield.contains(shieldItem.getItemId()))
		{
			return true;
		}

		return false;
	}

	public boolean containShield(int shield_id)
	{
		if (_shield.isEmpty())
		{
			return false;
		}

		return _shield.contains(shield_id);
	}

	/**
	 * Checks if all parts of set are enchanted to +6 or more
	 * 
	 * @param player
	 * @return
	 */
	public int getEnchantLevel(Player player)
	{
		// Player don't have full set
		if (!containAll(player))
		{
			return 0;
		}

		Inventory inv = player.getInventory();

		ItemInstance chestItem = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		ItemInstance legsItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		ItemInstance headItem = inv.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
		ItemInstance glovesItem = inv.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		ItemInstance feetItem = inv.getPaperdollItem(Inventory.PAPERDOLL_FEET);

		int value = -1;
		if (!_chests.isEmpty())
		{
			value = value > -1 ? Math.min(value, chestItem.getFixedEnchantLevel(player)) : chestItem.getFixedEnchantLevel(player);
		}

		if (!_legs.isEmpty())
		{
			value = value > -1 ? Math.min(value, legsItem.getFixedEnchantLevel(player)) : legsItem.getFixedEnchantLevel(player);
		}

		if (!_gloves.isEmpty())
		{
			value = value > -1 ? Math.min(value, glovesItem.getFixedEnchantLevel(player)) : glovesItem.getFixedEnchantLevel(player);
		}

		if (!_head.isEmpty())
		{
			value = value > -1 ? Math.min(value, headItem.getFixedEnchantLevel(player)) : headItem.getFixedEnchantLevel(player);
		}

		if (!_feet.isEmpty())
		{
			value = value > -1 ? Math.min(value, feetItem.getFixedEnchantLevel(player)) : feetItem.getFixedEnchantLevel(player);
		}

		return value;
	}

	public List<Integer> getChestIds()
	{
		return _chests;
	}

	public List<Integer> getLegIds()
	{
		return _legs;
	}

	public List<Integer> getHeadIds()
	{
		return _head;
	}

	public List<Integer> getGlovesIds()
	{
		return _gloves;
	}

	public List<Integer> getFeetIds()
	{
		return _feet;
	}

	public List<Integer> getShieldIds()
	{
		return _shield;
	}
}