package l2s.gameserver.skills.skillclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.templates.StatsSet;

public class ChainHeal extends Skill
{
	private final int[] _healPercents;
	private final int _healRadius;
	private final int _maxTargets;

	public ChainHeal(StatsSet set)
	{
		super(set);
		_healRadius = set.getInteger("healRadius", 350);
		String[] params = set.getString("healPercents", "").split(";");
		_maxTargets = params.length;
		_healPercents = new int[params.length];
		for (int i = 0; i < params.length; i++)
			_healPercents[i] = Integer.parseInt(params[i]);
	}

	public boolean checkCondition(SkillEntry skillEntry, Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first, boolean sendMsg, boolean trigger)
	{
	    if (activeChar.isPlayable() && activeChar.getPlayer().isInFightClub() && !activeChar.getPlayer().getFightClubEvent().canUsePositiveMagic(activeChar, target))
	        return false; 
		if (!super.checkCondition(skillEntry, activeChar, target, forceUse, dontMove, first, sendMsg, trigger))
			return false;

		if (!checkMainTarget(activeChar, target))
		{
			activeChar.getPlayer().sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		return true;
	}

	@Override
	public void onEndCast(Creature activeChar, Set<Creature> targets)
	{
		super.onEndCast(activeChar, targets);

		int curTarget = 0;
		for (Creature target : targets)
		{
			if (target == null || target.isHealBlocked())
				continue;

			double hp = _healPercents[curTarget] * target.getMaxHp() / 100.;
			double addToHp = Math.max(0, Math.min(hp, target.getStat().calc(Stats.HP_LIMIT, null, null) * target.getMaxHp() / 100. - target.getCurrentHp()));

			if (addToHp > 0)
				target.setCurrentHp(addToHp + target.getCurrentHp(), false);

			if (target.isPlayer())
				if (activeChar != target)
					target.sendPacket(new SystemMessage(SystemMessage.XS2S_HP_HAS_BEEN_RESTORED_BY_S1).addString(activeChar.getName()).addNumber(Math.round(addToHp)));
				else
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HPS_HAVE_BEEN_RESTORED).addNumber(Math.round(addToHp)));

			curTarget++;
		}
	}

	private boolean checkMainTarget(Creature activeChar, Creature target)
	{
		// @Rivelia.
		// If the target is ourself and we are not heal blocked, it should heal because
		// we are main target.
		// The heal won't apply if our main target:
		// - Is heal blocked (see UseSkill).
		// - Is a door.
		// - Is a monster.
		// - Is in duel.
		// - Is curse weapon equipped.
		// - Is not friendly (same party/clan/ally).
		// - Is auto attackable (oly fix).
		if (target == null)
			return false;

		if (activeChar == target)
			return true;

		if (target.isDoor() || target.isMonster() || activeChar.isAutoAttackable(target))
			return false;

		if (target.isPlayer())
		{
			Player activeCharTarget = target.getPlayer();
			Player activeCharPlayer = activeChar.getPlayer();

			// @Rivelia. Do not accept target in duel if it's not ourself, is curse weapon
			// equipped or is not friendly.
			if ((activeCharTarget.isInDuel() && activeCharPlayer.getObjectId() != activeCharTarget.getObjectId()) || (activeCharPlayer != null && !isTargetFriendly(activeCharPlayer, activeCharTarget)))
				return false;
		}
		return true;
	}

	private boolean isTargetFriendly(Player activeCharPlayer, Player activeCharTarget)
	{
		/*
		 * На оффе можно хилить даже несопартийцев. boolean _party =
		 * activeCharTarget.isInParty(); boolean _partySelf =
		 * activeCharPlayer.isInParty(); boolean _isInSameParty = false; boolean _clan =
		 * activeCharTarget.getClan() == null ? false : true; boolean _clanSelf =
		 * activeCharPlayer.getClan() == null ? false : true; boolean _isInSameClan =
		 * false; boolean _ally = activeCharTarget.getAlliance() == null ? false : true;
		 * boolean _allySelf = activeCharPlayer.getAlliance() == null ? false : true;
		 * boolean isInSameAlly = false; if(_party && _partySelf) for(Player member :
		 * activeCharPlayer.getParty().getPartyMembers()) if(member == activeCharTarget)
		 * _isInSameParty = true; if(_clan && _clanSelf) for (Player clanMember :
		 * activeCharPlayer.getClan().getOnlineMembers()) if(clanMember ==
		 * activeCharTarget) _isInSameClan = true; if(_ally && _allySelf)
		 * if(activeCharPlayer.getClan().getAllyId() ==
		 * activeCharTarget.getClan().getAllyId() &&
		 * activeCharPlayer.getClan().getAllyId() != 0) isInSameAlly = true;
		 * if(!isInSameAlly && !_isInSameClan && !_isInSameParty) return false;
		 */

		return true;
	}

	@Override
	public Set<Creature> getTargets(SkillEntry skillEntry, Creature activeChar, Creature aimingTarget, boolean forceUse)
	{
		Set<Creature> result = new HashSet<Creature>();
		List<Creature> targets = aimingTarget.getAroundCharacters(_healRadius, 128);

		List<HealTarget> healTargets = new ArrayList<HealTarget>();

		// if(checkMainTarget(activeChar,aimingTarget))
		healTargets.add(new HealTarget(-100.0D, aimingTarget));

		Player activeCharPlayer = null;
		if (activeChar.isPlayer())
			activeCharPlayer = activeChar.getPlayer();

		// @Rivelia. Do not do the following if:
		// - targets is null or empty.
		// - Active Char is a player and is in duel.
		if (targets != null && !targets.isEmpty() && ((activeCharPlayer != null && !activeCharPlayer.isInDuel()) || activeCharPlayer == null))
		{
			for (Creature target : targets)
			{
				// @Rivelia. If the main target is not self, ChainHeal effect won't heal
				// ourself.
				// The heal won't apply if our target:
				// - Is heal blocked (see UseSkill).
				// - Is ourself.
				// - Is a door.
				// - Is a monster.
				// - Is a pet/summon that is not friendly (same party/clan/ally).
				// - Is in duel.
				// - Is curse weapon equipped.
				// - Is not friendly (same party/clan/ally).
				// - Is auto attackable (oly fix).
				// - Is dead.
				if (target == null)
					continue;

				if (target.getObjectId() == activeChar.getObjectId())
				{
					if (activeChar.getObjectId() != aimingTarget.getObjectId())
						continue;
				}
				else if (target.isDoor() || target.isMonster() || target.isAutoAttackable(activeChar) || target.isAlikeDead())
					continue;

				if (target.isSummon() || target.isPet())
				{
					Player owner = target.getPlayer();
					if (owner != null)
						if (activeCharPlayer != null && !isTargetFriendly(activeCharPlayer, owner) && owner.getObjectId() != activeCharPlayer.getObjectId())
							continue;
				}
				else if (target.isPlayer())
				{
					Player activeCharTarget = target.getPlayer();

					if (activeCharTarget.isInDuel() || (activeCharPlayer != null && !isTargetFriendly(activeCharPlayer, activeCharTarget)))
						continue;
				}
				double hpPercent = target.getCurrentHp() / target.getMaxHp();

				healTargets.add(new HealTarget(hpPercent, target));
			}
		}

		HealTarget[] healTargetsArr = new HealTarget[healTargets.size()];
		healTargets.toArray(healTargetsArr);
		Arrays.sort(healTargetsArr, (o1, o2) ->
		{
			if (o1 == null || o2 == null)
				return 0;
			if (o1.getHpPercent() < o2.getHpPercent())
				return -1;
			if (o1.getHpPercent() > o2.getHpPercent())
				return 1;
			return 0;
		});

		int targetsCount = 0;
		for (HealTarget ht : healTargetsArr)
		{
			result.add(ht.getTarget());
			targetsCount++;
			if (targetsCount >= _maxTargets)
				break;
		}
		return result;
	}

	private static class HealTarget
	{
		private final double hpPercent;
		private final Creature target;

		public HealTarget(double hpPercent, Creature target)
		{
			this.hpPercent = hpPercent;
			this.target = target;
		}

		public double getHpPercent()
		{
			return hpPercent;
		}

		public Creature getTarget()
		{
			return target;
		}
	}
}