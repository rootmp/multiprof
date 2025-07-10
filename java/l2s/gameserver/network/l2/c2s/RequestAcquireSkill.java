package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.data.xml.holder.SkillAcquireHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.model.actor.instances.player.ShortCut;
import l2s.gameserver.model.base.AcquireType;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.instances.VillageMasterPledgeBypasses;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.SubUnit;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.item.data.AlterItemData;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.MulticlassUtils;

public class RequestAcquireSkill extends L2GameClientPacket
{
	private AcquireType _type;
	private int _id, _level, _subUnit;

	@Override
	protected boolean readImpl()
	{
		_id = readD();
		_level = readD();
		_type = AcquireType.getById(readD());
		if (_type == AcquireType.SUB_UNIT)
		{
			_subUnit = readD();
		}
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null || player.isTransformed() || _type == null)
		{
			return;
		}

		NpcInstance trainer = player.getLastNpc();
		if ((trainer == null || !player.checkInteractionDistance(trainer)) && !player.isGM())
		{
			trainer = null;
		}

		SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, _id, _level);
		if (skillEntry == null)
		{
			return;
		}

		ClassId selectedMultiClassId = player.getSelectedMultiClassId();
		if (_type == AcquireType.MULTICLASS)
		{
			if (selectedMultiClassId == null)
			{
				return;
			}
		}
		else
		{
			selectedMultiClassId = null;
		}

		if (!SkillAcquireHolder.getInstance().isSkillPossible(player, selectedMultiClassId, skillEntry.getTemplate(), _type))
		{
			return;
		}

		SkillLearn skillLearn = SkillAcquireHolder.getInstance().getSkillLearn(player, selectedMultiClassId, _id, _level, _type);
		if (skillLearn == null)
		{
			return;
		}
		if (skillLearn.getMinLevel() > player.getLevel())
		{
			return;
		}
		if (!checkSpellbook(player, _type, skillLearn, false))
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_NECESSARY_MATERIALS_OR_PREREQUISITES_TO_LEARN_THIS_SKILL);
			return;
		}

		switch (_type)
		{
			case NORMAL:
				learnSimpleNextLevel(player, _type, skillLearn, skillEntry, true);
				break;
			case CERTIFICATION:
				if (trainer != null)
				{
					learnSimpleNextLevel(player, _type, skillLearn, skillEntry, true);
					NpcInstance.showAcquireList(AcquireType.CERTIFICATION, player);
				}
				break;
			case FISHING:
				if (trainer != null)
				{
					learnSimpleNextLevel(player, _type, skillLearn, skillEntry, false);
					NpcInstance.showFishingSkillList(player);
				}
				break;
			case CLAN:
				if (trainer != null)
					learnClanSkill(player, skillLearn, trainer, skillEntry);
				break;
			case SUB_UNIT:
				if (trainer != null)
					learnSubUnitSkill(player, skillLearn, trainer, skillEntry, _subUnit);
				break;
			case MULTICLASS:
				learnSimpleNextLevel(player, _type, skillLearn, skillEntry, true);
				MulticlassUtils.showMulticlassAcquireList(player, selectedMultiClassId);
				break;
			case CUSTOM:
				player.getListeners().onLearnCustomSkill(skillLearn);
				break;
		}
	}

	/**
	 * learnSimpleNextLevel
	 * 
	 * @param player
	 * @param type
	 * @param skillLearn
	 * @param skillEntry
	 * @param normal
	 */
	private static void learnSimpleNextLevel(Player player, AcquireType type, SkillLearn skillLearn, SkillEntry skillEntry, boolean normal)
	{
		final int skillLevel = player.getSkillLevel(skillLearn.getId(), 0);
		if (skillLevel != skillLearn.getLevel() - 1)
		{
			return;
		}
		learnSimple(player, type, skillLearn, skillEntry, normal);
	}

	/**
	 * learnSimple
	 * 
	 * @param player
	 * @param type
	 * @param skillLearn
	 * @param skillEntry
	 * @param normal
	 */
	private static void learnSimple(Player player, AcquireType type, SkillLearn skillLearn, SkillEntry skillEntry, boolean normal)
	{
		if (player.getSp() < skillLearn.getCost())
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_SP_TO_LEARN_THIS_SKILL);
			return;
		}

		player.getInventory().writeLock();
		try
		{
			if (!checkSpellbook(player, type, skillLearn, true))
			{
				return;
			}
		}
		finally
		{
			player.getInventory().writeUnlock();
		}

		player.sendPacket(new SystemMessagePacket(SystemMsg.YOU_HAVE_EARNED_S1_SKILL).addSkillName(skillEntry.getId(), skillEntry.getLevel()));

		// if skill replaces one another skill - updating skill list
		if ((skillEntry.getTemplate().getSkillsToReplace().size() > 0) && (skillEntry.getTemplate().getSkillsToAdd().size() > 0))
		{
			for (SkillEntry skillToReplace : player.getAllSkills())
			{
				int i = 0;
				for (int replacedSkill : skillEntry.getTemplate().getSkillsToReplace().toArray())
				{
					if (skillToReplace.getId() == replacedSkill)
					{
						int addSkillId = skillEntry.getTemplate().getSkillsToAdd().toArray()[i];
						player.addSkill(SkillEntry.makeSkillEntry(SkillEntryType.NONE, addSkillId, skillToReplace.getLevel()), true);
						for (ShortCut sc : player.getAllShortCuts())
						{
							if (sc.getId() == skillToReplace.getId())
							{
								sc.setId(addSkillId);
								player.updateSkillShortcuts(addSkillId, skillToReplace.getLevel());
							}
						}
					}
					i++;
				}
			}
		}

		if ((skillEntry.getTemplate().getUpgradedSkillId() > 0) && (skillEntry.getTemplate().getUpgradeControllerSkillId() > 0))
		{
			for (SkillEntry sk : player.getAllSkills())
			{
				if (sk.getId() == skillEntry.getTemplate().getUpgradeControllerSkillId())
				{
					player.addSkill(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillEntry.getTemplate().getUpgradedSkillId(), skillEntry.getLevel()), true);
					for (ShortCut sc : player.getAllShortCuts())
					{
						if ((sc.getId() == skillEntry.getId()) || (sc.getId() == skillEntry.getTemplate().getUpgradedSkillId()))
						{
							sc.setId(skillEntry.getTemplate().getUpgradedSkillId());
							player.updateSkillShortcuts(skillEntry.getTemplate().getUpgradedSkillId(), skillEntry.getLevel());
						}
					}
					break;
				}
			}
		}

		player.setSp(player.getSp() - skillLearn.getCost());
		player.addSkill(skillEntry, true);

		if (normal)
		{
			player.rewardSkills(false);
		}

		player.sendUserInfo();
		player.updateStats();
		player.sendSkillList(skillEntry.getId());
		player.updateSkillShortcuts(skillEntry.getId(), skillEntry.getLevel());
	}

	/**
	 * @param player
	 * @param skillLearn
	 * @param trainer
	 * @param skillEntry
	 */
	private static void learnClanSkill(Player player, SkillLearn skillLearn, NpcInstance trainer, SkillEntry skillEntry)
	{
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMsg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
			return;
		}

		Clan clan = player.getClan();
		final int skillLevel = clan.getSkillLevel(skillLearn.getId(), 0);
		if (skillLevel != skillLearn.getLevel() - 1)
		{
			return;
		}
		if (clan.getReputationScore() < skillLearn.getCost())
		{
			player.sendPacket(SystemMsg.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
			return;
		}

		player.getInventory().writeLock();
		try
		{
			if (!checkSpellbook(player, AcquireType.CLAN, skillLearn, true))
			{
				return;
			}
		}
		finally
		{
			player.getInventory().writeUnlock();
		}

		clan.incReputation(-skillLearn.getCost(), false, "AquireSkill: " + skillLearn.getId() + ", lvl " + skillLearn.getLevel());
		clan.addSkill(skillEntry, true);
		clan.broadcastToOnlineMembers(new SystemMessagePacket(SystemMsg.THE_CLAN_SKILL_S1_HAS_BEEN_ADDED).addSkillName(skillEntry.getTemplate()));

		VillageMasterPledgeBypasses.showClanSkillList(trainer, player);
	}

	/**
	 * @param player
	 * @param skillLearn
	 * @param trainer
	 * @param skillEntry
	 * @param id
	 */
	private static void learnSubUnitSkill(Player player, SkillLearn skillLearn, NpcInstance trainer, SkillEntry skillEntry, int id)
	{
		Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}

		SubUnit sub = clan.getSubUnit(id);
		if (sub == null)
		{
			return;
		}

		if ((player.getClanPrivileges() & Clan.CP_CL_TROOPS_FAME) != Clan.CP_CL_TROOPS_FAME)
		{
			player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}

		int lvl = sub.getSkillLevel(skillLearn.getId(), 0);
		if (lvl >= skillLearn.getLevel())
		{
			player.sendPacket(SystemMsg.THIS_SQUAD_SKILL_HAS_ALREADY_BEEN_ACQUIRED);
			return;
		}

		if (lvl != (skillLearn.getLevel() - 1))
		{
			player.sendPacket(SystemMsg.THE_PREVIOUS_LEVEL_SKILL_HAS_NOT_BEEN_LEARNED);
			return;
		}

		if (clan.getReputationScore() < skillLearn.getCost())
		{
			player.sendPacket(SystemMsg.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
			return;
		}

		player.getInventory().writeLock();
		try
		{
			if (!checkSpellbook(player, AcquireType.SUB_UNIT, skillLearn, true))
			{
				return;
			}
		}
		finally
		{
			player.getInventory().writeUnlock();
		}

		clan.incReputation(-skillLearn.getCost(), false, "AquireSkill2: " + skillLearn.getId() + ", lvl " + skillLearn.getLevel());
		sub.addSkill(skillEntry, true);
		player.sendPacket(new SystemMessagePacket(SystemMsg.THE_CLAN_SKILL_S1_HAS_BEEN_ADDED).addSkillName(skillEntry.getTemplate()));

		if (trainer != null)
		{
			NpcInstance.showSubUnitSkillList(player);
		}
	}

	private static boolean checkSpellbook(Player player, AcquireType type, SkillLearn skillLearn, boolean consume)
	{
		loop1: for (AlterItemData item : skillLearn.getRequiredItemsForLearn(type))
		{
			for (int itemId : item.getIds())
			{
				if (ItemFunctions.haveItem(player, itemId, item.getCount()))
				{
					continue loop1;
				}
			}
			return false;
		}

		if (consume)
		{
			for (AlterItemData item : skillLearn.getRequiredItemsForLearn(type))
			{
				for (int itemId : item.getIds())
				{
					if (ItemFunctions.deleteItem(player, itemId, item.getCount(), true))
						break;
				}
			}
		}
		return true;
	}
}