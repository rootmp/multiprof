package l2s.gameserver.handler.items.impl;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.SoulShotType;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.WeaponTemplate;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

public class SoulShotItemHandler extends DefaultItemHandler
{
	private static final TIntIntMap SHOT_SKILLS = new TIntIntHashMap();
	static
	{
		SHOT_SKILLS.put(ItemGrade.NONE.ordinal(), 2039); // None Grade
		SHOT_SKILLS.put(ItemGrade.D.ordinal(), 2150); // D Grade
		SHOT_SKILLS.put(ItemGrade.C.ordinal(), 2151); // C Grade
		SHOT_SKILLS.put(ItemGrade.B.ordinal(), 2152); // B Grade
		SHOT_SKILLS.put(ItemGrade.A.ordinal(), 2153); // A Grade
		SHOT_SKILLS.put(ItemGrade.S.ordinal(), 2154); // S Grade
		SHOT_SKILLS.put(ItemGrade.R.ordinal(), 9193); // R Grade
	};

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if (playable == null || !playable.isPlayer())
		{
			return false;
		}
		Player player = (Player) playable;

		// soulshot is already active
		if (player.getChargedSoulshotPower() > 0)
		{
			return false;
		}
		int shotId = item.getItemId();
		boolean isAutoSoulShot = false;

		if (player.isAutoShot(shotId))
		{
			isAutoSoulShot = true;
		}
		if (player.getActiveWeaponInstance() == null)
		{
			if (!isAutoSoulShot)
			{
				player.sendPacket(SystemMsg.CANNOT_USE_SOULSHOTS);
			}
			return false;
		}

		WeaponTemplate weaponItem = player.getActiveWeaponTemplate();

		int ssConsumption = weaponItem.getSoulShotCount();
		if (ssConsumption <= 0)
		{
			// Can't use soulshots
			if (isAutoSoulShot)
			{
				player.removeAutoShot(shotId, true, SoulShotType.SOULSHOT);
				return false;
			}
			player.sendPacket(SystemMsg.CANNOT_USE_SOULSHOTS);
			return false;
		}

		int[] reducedSoulshot = weaponItem.getReducedSoulshot();
		if (reducedSoulshot[0] > 0 && Rnd.chance(reducedSoulshot[0]))
		{
			ssConsumption = reducedSoulshot[1];
		}
		if (ssConsumption <= 0)
		{
			return false;
		}
		ItemGrade grade = weaponItem.getGrade().extGrade();
		if (!player.getInventory().destroyItem(item, ssConsumption))
		{
			if (isAutoSoulShot)
			{
				player.removeAutoShot(shotId, true, SoulShotType.SOULSHOT);
				return false;
			}
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_SOULSHOTS_FOR_THAT);
			return false;
		}

		SkillEntry skillEntry = player.getAdditionalSSEffect(false, false);
		if (skillEntry == null)
		{
			skillEntry = item.getTemplate().getFirstSkill();
		}
		if (skillEntry == null)
		{
			skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.CUNSUMABLE_ITEM, SHOT_SKILLS.get(grade.ordinal()), 1);
		}
		player.forceUseSkill(skillEntry, player);
		return true;
	}

	@Override
	public boolean isAutoUse()
	{
		return true;
	}
}