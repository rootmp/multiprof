package l2s.gameserver.handler.items.impl;

import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;

public class HarvesterItemHandler extends DefaultItemHandler
{
	private static final int HARVESTER_SKILL_ID = 2098;

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		Player player;
		if (playable.isPlayer())
			player = (Player) playable;
		else if (playable.isPet())
			player = playable.getPlayer();
		else
			return false;

		GameObject target = player.getTarget();
		if (target == null || !target.isMonster())
		{
			player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}

		MonsterInstance monster = (MonsterInstance) player.getTarget();

		if (!monster.isDead())
		{
			player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}

		SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.ITEM, HARVESTER_SKILL_ID, 1);
		if (skillEntry != null && skillEntry.checkCondition(player, monster, false, false, true))
		{
			player.getAI().Cast(skillEntry, monster);
			return true;
		}
		return false;
	}
}
