package l2s.gameserver.handler.items.impl;

import java.util.List;

import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.base.SoulShotType;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;

/**
 * @author Bonux
 **/
public class BeastSoulShotItemHandler extends DefaultItemHandler
{
	private static final int SHOT_SKILL_ID = 2033;

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;

		Player player = (Player) playable;

		int shotId = item.getItemId();
		boolean isAutoSoulShot = player.isAutoShot(shotId);

		int deadServitors = 0;

		List<Servitor> servitors = player.getServitors();
		if(!servitors.isEmpty())
		{
			for(Servitor servitor : servitors)
			{
				if(servitor.isDead())
				{
					deadServitors++;
					continue;
				}

				if(servitor.getChargedSoulshotPower() > 0)
					continue;

				if(!player.getInventory().destroyItem(item, servitor.getSoulshotConsumeCount()))
				{
					if(isAutoSoulShot)
					{
						player.removeAutoShot(shotId, true, SoulShotType.BEAST_SOULSHOT);
						return false;
					}
					player.sendPacket(SystemMsg.YOU_DONT_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_PETSERVITOR);
					return false;
				}

				SkillEntry skillEntry = servitor.getAdditionalSSEffect(false, false);
				if(skillEntry == null)
					skillEntry = item.getTemplate().getFirstSkill();
				if(skillEntry == null)
					skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.CUNSUMABLE_ITEM, SHOT_SKILL_ID, 1);

				servitor.forceUseSkill(skillEntry, servitor);
			}

			if(deadServitors == servitors.size() && !isAutoSoulShot)
			{
				player.sendPacket(SystemMsg.SOULSHOTS_AND_SPIRITSHOTS_ARE_NOT_AVAILABLE_FOR_A_DEAD_PET_OR_SERVITOR);
				return false;
			}
		}
		else if(!isAutoSoulShot)
		{
			player.sendPacket(SystemMsg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}
		return true;
	}

	@Override
	public boolean isAutoUse()
	{
		return true;
	}
}