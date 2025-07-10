package l2s.gameserver.skills.effects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.handler.effects.EffectHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.actor.instances.player.ShortCut;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author nexvill
 */
public final class EffectReplaceSkill extends EffectHandler
{
	private final int _skillAdd;
	private final int _skillReplace;
	private static final Logger _log = LoggerFactory.getLogger(EffectReplaceSkill.class);

	public EffectReplaceSkill(EffectTemplate template)
	{
		super(template);
		_skillAdd = getParams().getInteger("skillAdd", 0);
		_skillReplace = getParams().getInteger("skillReplace", 0);
	}

	@Override
	protected boolean checkCondition(Abnormal abnormal, Creature effector, Creature effected)
	{
		if (!effected.isPlayer())
			return false;

		Player player = effected.getPlayer();
		if (player == null)
			return false;

		if (effected.isDead() || effector.isDead())
			return false;

		return true;
	}

	@Override
	public void onStart(Abnormal abnormal, Creature effector, Creature effected)
	{
		Player player = effected.getPlayer();

		SkillEntry replacedSkill = player.getKnownSkill(_skillReplace);
		if (replacedSkill == null)
		{
			return;
		}

		if (player.isDisabledSkillToReplace(_skillReplace))
		{
			return;
		}

		if (player.getActiveReflection() != null)
		{
			if ((player.getActiveReflection().getId() < 208) || (player.getActiveReflection().getId() > 215))
			{
				if ((getSkill().getId() != 45197) && (getSkill().getId() != 45198))
				{
					return;
				}
			}
		}

		SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, _skillAdd, replacedSkill.getLevel());
		if (skillEntry != null)
		{
			player.addSkill(skillEntry, false);
			player.addHiddenSkill(_skillReplace);
			updateShortCut(player, _skillAdd, _skillReplace, replacedSkill.getLevel());
		}
		else
		{
			_log.warn("skillEntry null, id: " + _skillReplace);
		}
	}

	@Override
	public void onExit(Abnormal abnormal, Creature effector, Creature effected)
	{
		Player player = effected.getPlayer();

		SkillEntry replacedSkill = player.getKnownSkill(_skillAdd);
		if (replacedSkill == null)
		{
			return;
		}
		if (player.isDisabledSkillToReplace(_skillReplace))
		{
			return;
		}

		player.removeSkill(_skillAdd, false);
		player.removeHiddenSkill(_skillReplace);
		updateShortCut(player, _skillReplace, _skillAdd, replacedSkill.getLevel());
	}

	private void updateShortCut(Player player, int skillToAdd, int skillToRemove, int level)
	{
		for (ShortCut sc : player.getAllShortCuts())
		{
			if (sc.getId() == skillToRemove)
			{
				sc.setId(skillToAdd);
				player.updateSkillShortcuts(skillToAdd, level);
			}
		}
		player.sendUserInfo();
		player.updateStats();
		player.sendSkillList();
	}
}