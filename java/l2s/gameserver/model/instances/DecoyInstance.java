package l2s.gameserver.model.instances;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.lang.reference.HardReference;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.network.l2.s2c.AutoAttackStartPacket;
import l2s.gameserver.network.l2.s2c.ExCharInfo;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.npc.NpcTemplate;

public class DecoyInstance extends MonsterInstance
{
	private static final Logger _log = LoggerFactory.getLogger(DecoyInstance.class);

	private HardReference<Player> _playerRef;
	private int _lifeTime, _timeRemaining;
	private ScheduledFuture<?> _decoyLifeTask, _hateSpam;

	public DecoyInstance(int objectId, NpcTemplate template, Player owner, int lifeTime)
	{
		super(objectId, template, StatsSet.EMPTY);

		_playerRef = owner.getRef();
		_lifeTime = lifeTime;
		_timeRemaining = _lifeTime;
		int skilllevel = getNpcId() < 13257 ? getNpcId() - 13070 : getNpcId() - 13250;
		_decoyLifeTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new DecoyLifetime(), 1000, 1000);
		_hateSpam = ThreadPoolManager.getInstance().scheduleAtFixedRate(new HateSpam(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 5272, skilllevel)), 1000, 3000);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		if (_hateSpam != null)
		{
			_hateSpam.cancel(false);
			_hateSpam = null;
		}
		_lifeTime = 0;
	}

	class DecoyLifetime implements Runnable
	{
		@Override
		public void run()
		{
			double newTimeRemaining;
			decTimeRemaining(1000);
			newTimeRemaining = getTimeRemaining();
			if (newTimeRemaining < 0)
				unSummon();
		}
	}

	class HateSpam implements Runnable
	{
		private SkillEntry _skillEntry;

		HateSpam(SkillEntry skillEntry)
		{
			_skillEntry = skillEntry;
		}

		@Override
		public void run()
		{
			setTarget(DecoyInstance.this);
			doCast(_skillEntry, DecoyInstance.this, true);
		}
	}

	public void unSummon()
	{
		if (_decoyLifeTask != null)
		{
			_decoyLifeTask.cancel(false);
			_decoyLifeTask = null;
		}
		if (_hateSpam != null)
		{
			_hateSpam.cancel(false);
			_hateSpam = null;
		}
		deleteMe();
	}

	public void decTimeRemaining(int value)
	{
		_timeRemaining -= value;
	}

	public int getTimeRemaining()
	{
		return _timeRemaining;
	}

	public int getLifeTime()
	{
		return _lifeTime;
	}

	@Override
	public Player getPlayer()
	{
		return _playerRef == null ? null : _playerRef.get();
	}

	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		Player owner = getPlayer();
		return owner != null && owner.isAutoAttackable(attacker);
	}

	@Override
	public boolean isAttackable(Creature attacker)
	{
		Player owner = getPlayer();
		return owner != null && owner.isAttackable(attacker);
	}

	@Override
	protected void onDelete()
	{
		Player owner = getPlayer();
		if (owner != null)
			owner.removeDecoy(this);
		super.onDelete();
	}

	@Override
	public void onAction(Player player, boolean shift)
	{
		if (player.getTarget() != this)
			player.setTarget(this);
		else if (isAutoAttackable(player))
			player.getAI().Attack(this, false, shift);
	}

	@Override
	public double getCollisionRadius()
	{
		Player player = getPlayer();
		if (player == null)
			return 0;
		return player.getCollisionRadius();
	}

	@Override
	public double getCollisionHeight()
	{
		Player player = getPlayer();
		if (player == null)
			return 0;
		return player.getCollisionHeight();
	}

	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		if (!isInCombat())
			return Collections.<L2GameServerPacket>singletonList(new ExCharInfo(this, forPlayer));
		else
		{
			List<L2GameServerPacket> list = new ArrayList<L2GameServerPacket>(2);
			list.add(new ExCharInfo(this, forPlayer));
			list.add(new AutoAttackStartPacket(objectId));
			return list;
		}
	}

	@Override
	public boolean isPeaceNpc()
	{
		return false;
	}

	public void transferOwnerBuffs()
	{
		Collection<Abnormal> abnormals = getPlayer().getAbnormalList().values();
		for (Abnormal a : abnormals)
		{
			Skill skill = a.getSkill();
			if (a.isOffensive() || skill.isToggle() || skill.isCubicSkill())
				continue;

			Abnormal abnormal = new Abnormal(a.getEffector(), this, a);
			abnormal.setDuration(a.getDuration());
			abnormal.setTimeLeft(a.getTimeLeft());
			getAbnormalList().add(abnormal);
		}
	}
}