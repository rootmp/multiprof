package l2s.gameserver.model.actor.instances.player;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.actor.instances.player.ShortCut.ShortCutType;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.base.ItemAutouseType;
import l2s.gameserver.model.instances.SummonInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.RequestActionUse.Action;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ActionFailPacket;
import l2s.gameserver.network.l2.s2c.ExActivateAutoShortcut;
import l2s.gameserver.network.l2.s2c.ExAutoplayDoMacro;
import l2s.gameserver.network.l2.s2c.ShortCutInitPacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.EffectUseType;
import l2s.gameserver.skills.enums.SkillAutoUseType;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.skills.enums.SkillType;
import l2s.gameserver.utils.ItemFunctions;

public class AutoShortCuts
{
	private enum AutoUseType
	{
		HEAL,
		MANA_HEAL,
		ITEM_BUFF,
		SKILL_BUFF,
		SKILL_ATTACK,
		ATTACK,
		PET_SKILL;
	}

	private final Player _owner;

	private int autoHealItem = -1;
	private int autoManaHealItem = -1;
	private final ReentrantLock writeLock = new ReentrantLock();
	private final Set<Integer> _autoBuffItems = new CopyOnWriteArraySet<>();
	private final Set<Integer> _autoBuffSkills = new CopyOnWriteArraySet<>();
	private final Set<Integer> _autoPetSkills = new CopyOnWriteArraySet<>();
	private final Set<Integer> _autoAttackSkills = new CopyOnWriteArraySet<>();
	private boolean _autoAttack = false;
	private boolean _autoAttackPet = false;

	private ScheduledFuture<?> _autoHealTask = null;
	private ScheduledFuture<?> _autoManaHealTask = null;
	private ScheduledFuture<?> _autoBuffItemsTask = null;
	private ScheduledFuture<?> _autoBuffSkillsTask = null;
	private ScheduledFuture<?> _autoPetSkillsTask = null;
	private ScheduledFuture<?> _autoAttackSkillsTask = null;
	private ScheduledFuture<?> _autoAttackTask = null;
	private ScheduledFuture<?> _autoAttackPetTask = null;
	private Action action;

	public AutoShortCuts(Player owner)
	{
		_owner = owner;
	}

	public boolean autoSkillsActive()
	{
		return _autoAttackSkillsTask != null;
	}

	public boolean autoAttackActive()
	{
		return _autoAttackTask != null;
	}

	private void doAutoShortCut(AutoUseType autoUseType)
	{
		writeLock.lock();
		try
		{
			if (_owner.isInPeaceZone() && !Config.AUTOFARM_IN_PEACE_ZONE)
				return;

			if (_owner.isInPeaceZone() && (_owner.getTarget() != null) && (!_owner.getTarget().isMonster()))
				return;

			if (autoUseType == AutoUseType.HEAL)
			{
				if (_owner.getCurrentHpPercents() >= _owner.getVarInt(PlayerVariables.AUTO_HP_VAR, 80))
					return;

				final ItemInstance item = _owner.getInventory().getItemByObjectId(autoHealItem);
				if (item == null)
				{
					autoHealItem = -1;
					stopAutoHealTask();
					return;
				}
				if (item.getTemplate().getFirstSkill() == null)
				{
					activate(1, ShortCut.PAGE_AUTOPLAY, false, true);
					return;
				}
				_owner.useItem(item, false, false);
			}
			if (autoUseType == AutoUseType.MANA_HEAL)
			{
				if (_owner.getCurrentMpPercents() >= _owner.getVarInt(PlayerVariables.AUTO_MP_VAR, 80))
					return;

				final ItemInstance item = _owner.getInventory().getItemByObjectId(autoManaHealItem);
				if (item == null)
				{
					autoManaHealItem = -1;
					stopAutoManaHealTask();
					return;
				}
				if (item.getTemplate().getFirstSkill() == null)
				{
					activate(2, ShortCut.PAGE_AUTOPLAY, false, true);
					return;
				}
				_owner.useItem(item, false, false);
			}
			else if (autoUseType == AutoUseType.ITEM_BUFF)
			{
				loop: for (final int itemObjectId : _autoBuffItems)
				{
					final ItemInstance item = _owner.getInventory().getItemByObjectId(itemObjectId);
					if (item == null)
					{
						_autoBuffItems.remove(itemObjectId);
						continue;
					}

					final SkillEntry skillEntry = item.getTemplate().getFirstSkill();
					final Skill skill = skillEntry.getTemplate();
					Creature target = skill.getAimingTarget(_owner, _owner);
					if (target == null)
					{
						target = _owner;
					}

					for (final Abnormal abnormal : target.getAbnormalList())
					{
						if (!abnormal.canReplaceAbnormal(skill, 1) && !skill.hasEffects(EffectUseType.NORMAL_INSTANT))
						{
							continue loop;
						}
					}
					_owner.useItem(item, false, false);
				}

				if (_autoBuffItems.isEmpty())
				{
					stopAutoBuffItemsTask();
				}
			}
			else if (autoUseType == AutoUseType.SKILL_BUFF)
			{
				loop: for (final int skillId : _autoBuffSkills)
				{
					final SkillEntry skillEntry = _owner.getKnownSkill(skillId);
					if (skillEntry == null)
					{
						_autoBuffSkills.remove(skillId);
						continue;
					}

					final Skill skill = skillEntry.getTemplate();
					final Creature target = skill.getAimingTarget(_owner, _owner);
					if (target == null)
					{
						continue;
					}

					for (final Abnormal abnormal : target.getAbnormalList())
					{
						if (!abnormal.canReplaceAbnormal(skill, 1))
						{
							continue loop;
						}
					}
					if ((skill.getItemConsumeId() > 0) && (skill.getItemConsume() > 0))
					{
						if (ItemFunctions.getItemCount(_owner, skill.getItemConsumeId()) >= skill.getItemConsume())
						{
							_owner.getAI().Cast(skillEntry, target, false, false);
						}
						else
						{
							_autoBuffSkills.remove(skillId);
							for (final ShortCut shortCut : _owner.getAllShortCuts())
							{
								if (shortCut.getId() == skillId)
								{
									_owner.sendPacket(new ExActivateAutoShortcut(shortCut.getSlot(), shortCut.getPage(), false));
									shortCut.setAutoUse(false);
									_owner.registerShortCut(shortCut);
									_owner.sendPacket(new ShortCutInitPacket(_owner));
								}
							}
						}
					}
					else
					{
						_owner.getAI().Cast(skillEntry, target, false, false);
					}
				}

				if (_autoBuffSkills.isEmpty())
				{
					stopAutoBuffSkillsTask();
				}
			}
			else if ((autoUseType == AutoUseType.SKILL_ATTACK) && _owner.getPlayer().getAutoFarm().isFarmActivate())
			{
				if (_autoAttackSkills.size() == 0)
					return;

				final int index = new Random().nextInt(_autoAttackSkills.size());
				final Iterator<Integer> iter = _autoAttackSkills.iterator();
				for (int i = 0; i < index; i++)
				{
					iter.next();
				}
				final int skillId = iter.next();
				final SkillEntry skillEntry = _owner.getKnownSkill(skillId);
				if (skillEntry == null)
				{
					_autoAttackSkills.remove(skillId);
					return;
				}

				final Skill skill = skillEntry.getTemplate();
				Creature target = null;
				if (skill.getSkillType() == SkillType.HEAL)
				{
					target = skill.getAimingTarget(_owner, _owner);
					_owner.getAI().Cast(skillEntry, target, true, false);
				}
				else
				{
					target = skill.getAimingTarget(_owner, _owner.getTarget());
					if ((skill.getMpConsume() <= _owner.getCurrentMp()) && ((skill.getHpConsume() + 1) < _owner.getCurrentHp()) && (skill.getDpConsume() <= _owner.getCurrentDp()))
					{
						boolean isDarkVeilActive = false;
						for (final Abnormal ab : _owner.getAbnormalList().toArray())
						{
							if ((ab.getSkill().getId() == 398) && (ab.getSkill().getLevel() == 2))
							{
								isDarkVeilActive = true;
							}
						}
						if ((skillEntry.getId() == 45161) && isDarkVeilActive)
						{
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 45162, skillEntry.getLevel()), target, false, false);
						}
						else if ((skillEntry.getId() == 45163) && isDarkVeilActive)
						{
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 45164, skillEntry.getLevel()), target, false, false);
						}
						if (skillEntry.getId() == 47011 && target != null && _owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47012, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47011 && target != null && !_owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47013, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47002 && target != null && _owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47003, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47002 && target != null && !_owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47004, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47005 && target != null && _owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47006, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47005 && target != null && !_owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47007, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47008 && target != null && _owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47009, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47008 && target != null && !_owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47010, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47015 && target != null && _owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47016, skillEntry.getLevel()), target, false, false);
						}
						else if (skillEntry.getId() == 47015 && target != null && !_owner.isInRange(target.getLoc(), 200))
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
							_owner.getAI().Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 47017, skillEntry.getLevel()), target, false, false);
						}
						else if ((skill.getItemConsumeId() > 0) && (skill.getItemConsume() > 0))
						{
							if ((ItemFunctions.getItemCount(_owner, skill.getItemConsumeId()) < skill.getItemConsume()) && !_owner.isMageClass())
							{
								_owner.sendPacket(new ExAutoplayDoMacro());
							}
						}
						else
						{
							_owner.getAI().Cast(skillEntry, target, true, false);
						}
					}
					else if (!_owner.isMageClass())
					{
						_owner.sendPacket(new ExAutoplayDoMacro());
					}
					if (target == null)
						return;
				}

				if (_autoAttackSkills.isEmpty())
				{
					stopAutoAttackSkillsTask();
				}
			}
			else if ((autoUseType == AutoUseType.PET_SKILL) && _owner.getPlayer().getAutoFarm().isFarmActivate())
			{
				for (final int skillId : _autoPetSkills)
				{
					final SummonInstance summon = _owner.getSummon();
					if (summon == null || summon.isOutOfControl())
						return;

					if (!servitorUseSkill(_owner, summon, skillId, action.id))
					{
						_owner.sendActionFailed();
					}
				}

				if (_autoPetSkills.isEmpty())
				{
					stopAutoPetSkillsTask();
				}
			}
			else if ((autoUseType == AutoUseType.ATTACK) && _owner.getPlayer().getAutoFarm().isFarmActivate())
			{
				if (_autoAttack)
				{
					_owner.sendPacket(new ExAutoplayDoMacro());
				}
				if (_autoAttackPet && (_owner.getTarget() != null) && (_owner.getTarget().isCreature()))
				{
					for (final Servitor summon : _owner.getServitors())
					{
						summon.getAI().Attack(_owner.getTarget(), true, false);
					}
					if (_owner.getPet() != null)
					{
						_owner.getPet().getAI().Attack(_owner.getTarget(), true, false);
					}
				}

				if (!_autoAttack)
				{
					stopAutoAttackTask();
				}
				if (!_autoAttackPet)
				{
					stopAutoAttackPetTask();
				}
			}
		}
		finally
		{
			writeLock.unlock();
		}
	}

	public boolean activate(int slotIndex, boolean active)
	{
		if (slotIndex == 65535)
		{
			boolean success = false;
			for (int s = 0; s < 12; s++)
			{
				if (activate(s, ShortCut.PAGE_AUTOCONSUME, active, false))
				{
					success = true;
				}
			}
			return success;
		}

		final int slot = slotIndex % 12;
		final int page = slotIndex / 12;
		return activate(slot, page, active, true);
	}

	public synchronized boolean activate(int slot, int page, boolean active, boolean checkPage)
	{
		if (activate0(slot, page, active, checkPage))
		{
			if (autoHealItem > 0)
			{
				if (_autoHealTask == null)
				{
					_autoHealTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(() -> doAutoShortCut(AutoUseType.HEAL), 0, 250L);
				}
			}
			else
			{
				stopAutoHealTask();
			}
			if (autoManaHealItem > 0)
			{
				if (_autoManaHealTask == null)
				{
					_autoManaHealTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(() -> doAutoShortCut(AutoUseType.MANA_HEAL), 0, 250L);
				}
			}
			else
			{
				stopAutoManaHealTask();
			}
			if (!_autoBuffItems.isEmpty())
			{
				if (_autoBuffItemsTask == null)
				{
					_autoBuffItemsTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(() -> doAutoShortCut(AutoUseType.ITEM_BUFF), 0, 1000L);
				}
			}
			else
			{
				stopAutoBuffItemsTask();
			}
			if (!_autoBuffSkills.isEmpty())
			{
				if (_autoBuffSkillsTask == null)
				{
					_autoBuffSkillsTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(() -> doAutoShortCut(AutoUseType.SKILL_BUFF), 0, 1000L);
				}
			}
			else
			{
				stopAutoBuffSkillsTask();
			}
			if (!_autoAttackSkills.isEmpty())
			{
				if (_autoAttackSkillsTask == null)
				{
					_autoAttackSkillsTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(() -> doAutoShortCut(AutoUseType.SKILL_ATTACK), 0, 500L);
				}
			}
			else
			{
				stopAutoAttackSkillsTask();
			}
			if (!_autoPetSkills.isEmpty())
			{
				if (_autoPetSkillsTask == null)
				{
					_autoPetSkillsTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(() -> doAutoShortCut(AutoUseType.PET_SKILL), 0, 500L);
				}
			}
			else
			{
				stopAutoPetSkillsTask();
			}
			if (_autoAttack)
			{
				if (_autoAttackTask == null)
				{
					_autoAttackTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(() -> doAutoShortCut(AutoUseType.ATTACK), 0, 500L);
				}
			}
			else
			{
				stopAutoAttackTask();
			}
			if (_autoAttackPet)
			{
				if (_autoAttackPetTask == null)
				{
					_autoAttackPetTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(() -> doAutoShortCut(AutoUseType.ATTACK), 0, 500L);
				}
			}
			else
			{
				stopAutoAttackPetTask();
			}

			return true;
		}
		return false;
	}

	private void stopAutoManaHealTask()
	{
		if (_autoManaHealTask != null)
		{
			_autoManaHealTask.cancel(false);
			_autoManaHealTask = null;
		}
	}

	private void stopAutoHealTask()
	{
		if (_autoHealTask != null)
		{
			_autoHealTask.cancel(false);
			_autoHealTask = null;
		}
	}

	private void stopAutoBuffItemsTask()
	{
		if (_autoBuffItemsTask != null)
		{
			_autoBuffItemsTask.cancel(false);
			_autoBuffItemsTask = null;
		}
	}

	private void stopAutoBuffSkillsTask()
	{
		if (_autoBuffSkillsTask != null)
		{
			_autoBuffSkillsTask.cancel(false);
			_autoBuffSkillsTask = null;
		}
	}

	private void stopAutoAttackSkillsTask()
	{
		if (_autoAttackSkillsTask != null)
		{
			_autoAttackSkillsTask.cancel(false);
			_autoAttackSkillsTask = null;
		}
	}

	private void stopAutoPetSkillsTask()
	{
		if (_autoPetSkillsTask != null)
		{
			_autoPetSkillsTask.cancel(false);
			_autoPetSkillsTask = null;
		}
	}

	private void stopAutoAttackTask()
	{
		if (_autoAttackTask != null)
		{
			_autoAttackTask.cancel(false);
			_autoAttackTask = null;
		}
	}

	private void stopAutoAttackPetTask()
	{
		if (_autoAttackPetTask != null)
		{
			_autoAttackPetTask.cancel(false);
			_autoAttackPetTask = null;
		}
	}

	private boolean activate0(int slot, int page, boolean active, boolean checkPage)
	{
		final ShortCut shortCut = _owner.getShortCut(slot, page);
		if ((shortCut == null) || !checkShortCut(shortCut.getSlot(), shortCut.getPage(), shortCut.getType(), shortCut.getId()))
			return false;

		if (page == ShortCut.PAGE_AUTOCONSUME)
		{
			if (active)
			{
				_autoBuffItems.add(shortCut.getId());
			}
			else
			{
				_autoBuffItems.remove(shortCut.getId());
			}
			if (checkPage)
			{
				for (int s = 0; s < 12; s++)
				{
					final ShortCut sc = _owner.getShortCut(s, page);
					if (sc != null && sc.getType() == shortCut.getType() && sc.getId() == shortCut.getId())
					{
						_owner.sendPacket(new ExActivateAutoShortcut(s, page, active));
						sc.setAutoUse(active);
						_owner.registerShortCut(sc);
						_owner.sendPacket(new ShortCutInitPacket(_owner));
					}
				}
			}
			else
			{
				_owner.sendPacket(new ExActivateAutoShortcut(slot, page, active));
				final ShortCut sc = _owner.getShortCut(slot, page);
				sc.setAutoUse(active);
				_owner.registerShortCut(sc);
				_owner.sendPacket(new ShortCutInitPacket(_owner));
			}
			return true;
		}
		else if (page == ShortCut.PAGE_AUTOPLAY)
		{
			if (slot == 1)
			{
				autoHealItem = active ? shortCut.getId() : -1;
				_owner.sendPacket(new ExActivateAutoShortcut(slot, page, active));
				final ShortCut sc = _owner.getShortCut(slot, page);
				sc.setAutoUse(active);
				_owner.registerShortCut(sc);
				_owner.sendPacket(new ShortCutInitPacket(_owner));
				return true;
			}
			else if (slot == 2)
			{
				autoManaHealItem = active ? shortCut.getId() : -1;
				_owner.sendPacket(new ExActivateAutoShortcut(slot, page, active));
				final ShortCut sc = _owner.getShortCut(slot, page);
				sc.setAutoUse(active);
				_owner.registerShortCut(sc);
				_owner.sendPacket(new ShortCutInitPacket(_owner));
				return true;
			}
		}
		else if (page >= ShortCut.PAGE_NORMAL_0 && page <= ShortCut.PAGE_FLY_TRANSFORM)
		{
			if (shortCut.getType() == ShortCutType.ITEM)
			{
				final ItemInstance item = _owner.getInventory().getItemByObjectId(shortCut.getId());
				if (item == null)
					return false;
				final SkillEntry skillEntry = item.getTemplate().getFirstSkill();
				if ((skillEntry != null) && item.getTemplate().getAutouseType() == ItemAutouseType.BUFF)
				{
					if (active)
					{
						_autoBuffItems.add(shortCut.getId());
					}
					else
					{
						_autoBuffItems.remove(shortCut.getId());
					}
				}
			}
			else if (shortCut.getType() != ShortCutType.ACTION)
			{
				final SkillEntry skillEntry = _owner.getKnownSkill(shortCut.getId());
				if ((skillEntry.getTemplate().getAutoUseType() == SkillAutoUseType.BUFF) || (skillEntry.getTemplate().getAutoUseType() == SkillAutoUseType.APPEARANCE))
				{
					if (active)
					{
						_autoBuffSkills.add(shortCut.getId());
					}
					else
					{
						_autoBuffSkills.remove(shortCut.getId());
					}
				}
				else if (skillEntry.getTemplate().getAutoUseType() == SkillAutoUseType.ATTACK)
				{
					if (active)
					{
						_autoAttackSkills.add(shortCut.getId());
					}
					else
					{
						_autoAttackSkills.remove(shortCut.getId());
					}
				}
			}
			else
			{
				if (active)
				{
					if (shortCut.getId() == 2)
					{
						_autoAttack = true;
					}
					else if ((shortCut.getId() == 16) || (shortCut.getId() == 22))
					{
						_autoAttackPet = true;
					}
					else
					{
						action = Action.find(shortCut.getId());
						if (action.value > 0)
						{
							_autoPetSkills.add(action.value);
						}
					}

				}
				else
				{
					if (shortCut.getId() == 2)
					{
						_autoAttack = false;
						stopAutoAttackTask();
					}
					else if ((shortCut.getId() == 16) || (shortCut.getId() == 22))
					{
						_autoAttackPet = false;
						stopAutoAttackPetTask();
					}
					else
					{
						action = Action.find(shortCut.getId());
						if (action.value > 0)
						{
							_autoPetSkills.remove(action.value);
						}
					}
				}
			}
			if (checkPage)
			{
				for (int p = ShortCut.PAGE_NORMAL_0; p <= ShortCut.PAGE_FLY_TRANSFORM; p++)
				{
					for (int s = 0; s < 12; s++)
					{
						final ShortCut sc = _owner.getShortCut(s, p);
						if (sc != null && sc.getType() == shortCut.getType() && sc.getId() == shortCut.getId())
						{
							_owner.sendPacket(new ExActivateAutoShortcut(s, p, active));
							sc.setAutoUse(active);
							_owner.registerShortCut(sc);
							_owner.sendPacket(new ShortCutInitPacket(_owner));
						}
					}
				}
			}
			else
			{
				_owner.sendPacket(new ExActivateAutoShortcut(slot, page, active));
				final ShortCut sc = _owner.getShortCut(slot, page);
				sc.setAutoUse(active);
				_owner.registerShortCut(sc);
				_owner.sendPacket(new ShortCutInitPacket(_owner));
			}
			return true;
		}
		return false;
	}

	public IBroadcastPacket canRegShortCut(int slot, int page, ShortCut.ShortCutType shortCutType, int id)
	{
		if (page >= ShortCut.PAGE_NORMAL_0 && page <= ShortCut.PAGE_FLY_TRANSFORM)
		{
		}
		else if (!checkShortCut(slot, page, shortCutType, id))
		{
			if (page == ShortCut.PAGE_AUTOPLAY && slot == 0)
				return SystemMsg.MACRO_USE_ONLY;
			else
				return ActionFailPacket.STATIC;
		}
		return null;
	}

	private boolean checkShortCut(int slot, int page, ShortCut.ShortCutType shortCutType, int id)
	{
		if (shortCutType == ShortCut.ShortCutType.MACRO)
		{
			if (page == ShortCut.PAGE_AUTOPLAY && slot == 0)
				return true;
		}
		else if (shortCutType == ShortCut.ShortCutType.ITEM)
		{
			ItemAutouseType autouseType;
			if (page == ShortCut.PAGE_AUTOPLAY)
			{
				if (slot != 1)
					return false;
				autouseType = ItemAutouseType.HEAL;
			}
			else if (page >= ShortCut.PAGE_NORMAL_0 && page <= ShortCut.PAGE_FLY_TRANSFORM)
			{
				autouseType = ItemAutouseType.BUFF;
			}
			else
				return false;

			final ItemInstance item = _owner.getInventory().getItemByObjectId(id);
			if (item == null || item.getTemplate().getAutouseType() != autouseType)
				return false;

			return true;
		}
		else if (shortCutType == ShortCut.ShortCutType.SKILL)
		{
			if (page >= ShortCut.PAGE_NORMAL_0 && page <= ShortCut.PAGE_FLY_TRANSFORM)
			{
				final SkillEntry skillEntry = _owner.getKnownSkill(id);
				if (skillEntry != null && (skillEntry.getTemplate().getAutoUseType() == SkillAutoUseType.BUFF //
						|| skillEntry.getTemplate().getAutoUseType() == SkillAutoUseType.APPEARANCE//
						|| skillEntry.getTemplate().getAutoUseType() == SkillAutoUseType.ATTACK))
					return true;
			}
		}
		else if (shortCutType == ShortCut.ShortCutType.ACTION)
			return true;
		return false;
	}

	private boolean servitorUseSkill(Player player, Servitor servitor, int skillId, int actionId)
	{
		writeLock.lock();
		try
		{
			if (servitor == null)
				return false;

			final int skillLevel = servitor.getActiveSkillLevel(skillId);
			if (skillLevel == 0)
				return false;

			final Skill skill = SkillHolder.getInstance().getSkill(skillId, skillLevel);
			if (skill == null)
				return false;

			if (servitor.isDepressed())
			{
				player.sendPacket(SystemMsg.YOUR_PETSERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
				return false;
			}

			if (servitor.isNotControlled()) // TODO: [Bonux] Проверить, распостраняется ли данное правило на саммонов.
			{
				player.sendPacket(SystemMsg.YOUR_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL);
				return false;
			}

			if (skill.getId() != 6054)
			{
				final int npcId = servitor.getNpcId();
				if (npcId == 16051 || npcId == 16052 || npcId == 16053 || npcId == 1601 || npcId == 1602 || npcId == 1603 || npcId == 1562 || npcId == 1563 || npcId == 1564 || npcId == 1565 || npcId == 1566 || npcId == 1567 || npcId == 1568 || npcId == 1569 || npcId == 1570 || npcId == 1571 || npcId == 1572 || npcId == 1573)
				{
					if (!servitor.getAbnormalList().contains(6054))
					{
						player.sendPacket(SystemMsg.A_PET_ON_AUXILIARY_MODE_CANNOT_USE_SKILLS);
						return false;
					}
				}
			}

			if (skill.isToggle())
			{
				if (servitor.getAbnormalList().contains(skill))
				{
					if (skill.isNecessaryToggle())
					{
						servitor.getAbnormalList().stop(skill.getId());
					}
					return true;
				}
			}

			if (skill.getTemplate().getAutoUseType() == SkillAutoUseType.BUFF)
			{
				for (final Abnormal abnormal : servitor.getAbnormalList())
				{
					if (!abnormal.canReplaceAbnormal(skill, 1))
						return false;
				}
			}

			final SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.SERVITOR, skill);
			if (skillEntry == null)
				return false;

			final Creature aimingTarget = skill.getAimingTarget(servitor, player.getTarget());

			if ((!player.getAutoFarm().isFarmActivate() && (skill.getTemplate().getAutoUseType() == SkillAutoUseType.ATTACK)) || !skill.checkCondition(skillEntry, servitor, aimingTarget, false, false, true, false, false) || servitor.isCastingNow())
				return false;

			servitor.setUsedSkill(skill, actionId); // TODO: [Bonux] Переделать.
			servitor.getAI().Cast(skillEntry, aimingTarget, false, false);
			return true;
		}
		finally
		{
			writeLock.unlock();
		}
	}
}
