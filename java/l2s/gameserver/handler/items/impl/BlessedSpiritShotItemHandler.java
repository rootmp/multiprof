package l2s.gameserver.handler.items.impl;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
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

public class BlessedSpiritShotItemHandler extends DefaultItemHandler
{
	private static final TIntIntMap SHOT_SKILLS = new TIntIntHashMap();
	static
	{
		SHOT_SKILLS.put(ItemGrade.NONE.ordinal(), 2061); // None Grade
		SHOT_SKILLS.put(ItemGrade.D.ordinal(), 2160); // D Grade
		SHOT_SKILLS.put(ItemGrade.C.ordinal(), 2161); // C Grade
		SHOT_SKILLS.put(ItemGrade.B.ordinal(), 2162); // B Grade
		SHOT_SKILLS.put(ItemGrade.A.ordinal(), 2163); // A Grade
		SHOT_SKILLS.put(ItemGrade.S.ordinal(), 2164); // S Grade
		SHOT_SKILLS.put(ItemGrade.R.ordinal(), 9195); // R Grade
	};

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;

		Player player = (Player) playable;

		// spiritshot is already active
		if(player.getChargedSpiritshotPower() > 0)
			return false;

		int shotId = item.getItemId();
		boolean isAutoSoulShot = false;

		if(player.isAutoShot(shotId))
			isAutoSoulShot = true;

		if(player.getActiveWeaponInstance() == null)
		{
			if(!isAutoSoulShot)
				player.sendPacket(SystemMsg.YOU_MAY_NOT_USE_SPIRITSHOTS);
			return false;
		}

		WeaponTemplate weaponItem = player.getActiveWeaponTemplate();

		int bspsConsumption = weaponItem.getSpiritShotCount();
		if(bspsConsumption <= 0)
		{
			// Can't use Spiritshots
			if(isAutoSoulShot)
			{
				player.removeAutoShot(shotId, true, SoulShotType.SPIRITSHOT);
				return false;
			}
			player.sendPacket(SystemMsg.YOU_MAY_NOT_USE_SPIRITSHOTS);
			return false;
		}

		int[] reducedSpiritshot = weaponItem.getReducedSpiritshot();
		if(reducedSpiritshot[0] > 0 && Rnd.chance(reducedSpiritshot[0]))
			bspsConsumption = reducedSpiritshot[1];

		if(bspsConsumption <= 0)
			return false;

		ItemGrade grade = weaponItem.getGrade().extGrade();
		/*
		 * if(grade != item.getGrade())//раскоментить если надо привязать соски к грейду
		 * оружия { // wrong grade for weapon if(isAutoSoulShot) return false;
		 * player.sendPacket(SystemMsg.YOUR_SPIRITSHOT_DOES_NOT_MATCH_THE_WEAPONS_GRADE)
		 * ; return false; }
		 */

		if(!player.getInventory().destroyItem(item, bspsConsumption))
		{
			if(isAutoSoulShot)
			{
				player.removeAutoShot(shotId, true, SoulShotType.SPIRITSHOT);
				return false;
			}
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_SPIRITSHOT_FOR_THAT);
			return false;
		}

		SkillEntry skillEntry = player.getAdditionalSSEffect(true, true);
		if(skillEntry == null)
			skillEntry = item.getTemplate().getFirstSkill();
		if(skillEntry == null)
			skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.CUNSUMABLE_ITEM, SHOT_SKILLS.get(grade.ordinal()), 1);

		player.forceUseSkill(skillEntry, player);
		return true;
	}

	@Override
	public boolean isAutoUse()
	{
		return true;
	}
}