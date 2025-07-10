package l2s.gameserver.model.actor.instances.player;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import l2s.gameserver.dao.CharacterHennaDAO;
import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.newhenna.ExNewHennaPotenEnchant;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.henna.HennaTemplate;
import l2s.gameserver.templates.henna.PotentialEffect;
import l2s.gameserver.templates.henna.PotentialFee;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.TimeUtils;

import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class HennaList implements Iterable<Henna>
{
	public static final int MAX_SIZE = 4;

	private static final String HIDDEN_POWER_STEP_VAR = "henna_hidden_power_step";
	private static final String HIDDEN_POWER_USED_VAR = "henna_hidden_power_used";

	private final Map<Integer, Henna> hennas = new TreeMap<>();
	private int _str, _int, _dex, _men, _wit, _con;

	private final Player owner;
	private final TIntObjectMap<SkillEntry> skills = new TIntObjectHashMap<>();

	public HennaList(Player owner)
	{
		this.owner = owner;
	}

	public void restore()
	{
		for (int i = 1; i <= MAX_SIZE; i++)
		{
			hennas.put(i, new Henna(owner, i));
		}
		CharacterHennaDAO.getInstance().select(owner, hennas);
		refreshStats(false);
	}

	public void store()
	{
		forEach(Henna::save);
	}

	public Henna get(int slot)
	{
		return hennas.get(slot);
	}

	public int size()
	{
		return hennas.size();
	}

	public Collection<Henna> values()
	{
		return hennas.values();
	}

	@Override
	public Iterator<Henna> iterator()
	{
		return values().iterator();
	}

	@Override
	public void forEach(Consumer<? super Henna> action)
	{
		values().forEach(action);
	}

	public int getINT()
	{
		return _int;
	}

	public int getSTR()
	{
		return _str;
	}

	public int getCON()
	{
		return _con;
	}

	public int getMEN()
	{
		return _men;
	}

	public int getWIT()
	{
		return _wit;
	}

	public int getDEX()
	{
		return _dex;
	}

	public boolean isActive(Henna henna)
	{
		if (henna.isActive())
		{
			HennaTemplate template = henna.getTemplate();
			return template != null && template.isForThisClass(owner);
		}
		return false;
	}

	public int getFreeSize()
	{
		int used = 0;
		for (Henna henna : values())
		{
			if (henna.getId() != 0)
				used++;
		}
		return MAX_SIZE - used;
	}

	public boolean refreshStats(boolean send)
	{
		_int = 0;
		_str = 0;
		_con = 0;
		_men = 0;
		_wit = 0;
		_dex = 0;

		boolean updateSkillList = false;
		for (int skillId : skills.keys())
		{
			if (owner.removeSkill(skillId, false) != null)
				updateSkillList = true;
		}

		skills.clear();

		for (Henna henna : values())
		{
			if (!isActive(henna))
				continue;

			HennaTemplate template = henna.getTemplate();

			_int += template.getStatINT();
			_str += template.getStatSTR();
			_men += template.getStatMEN();
			_con += template.getStatCON();
			_wit += template.getStatWIT();
			_dex += template.getStatDEX();

			for (TIntIntIterator iterator = template.getSkills().iterator(); iterator.hasNext();)
			{
				iterator.advance();

				SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, iterator.key(), iterator.value());
				if (skillEntry == null)
					continue;

				SkillEntry tempSkillEntry = skills.get(skillEntry.getId());
				if (tempSkillEntry == null || tempSkillEntry.getLevel() < skillEntry.getLevel())
					skills.put(skillEntry.getId(), skillEntry);
			}

			int activeStep = henna.getActiveStep();
			if (activeStep > 0)
			{
				PotentialEffect potentialEffect = HennaHolder.getInstance().getPotentialEffect(henna.getPotentialId());
				if (potentialEffect != null && potentialEffect.getSlotId() == henna.getSlot())
				{
					SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, potentialEffect.getSkillId(), Math.min(activeStep, potentialEffect.getMaxSkillLevel()));
					if (skillEntry != null)
					{
						SkillEntry tempSkillEntry = skills.get(skillEntry.getId());
						if (tempSkillEntry == null || tempSkillEntry.getLevel() < skillEntry.getLevel())
							skills.put(skillEntry.getId(), skillEntry);
					}
				}
			}
		}

		for (SkillEntry skillEntry : skills.valueCollection())
			owner.addSkill(skillEntry, false);

		if (!skills.isEmpty())
			updateSkillList = true;

		if (send)
		{
			owner.sendUserInfo(true);
		}

		return updateSkillList;
	}

	public int getHiddenPowerStep()
	{
		return owner.getVarInt(HIDDEN_POWER_STEP_VAR, 1);
	}

	public int getHiddenPowerLeft()
	{
		PotentialFee fee = HennaHolder.getInstance().getPotentialFee(getHiddenPowerStep());
		if (fee == null)
			return 0;
		return fee.getDailyCount() - owner.getVarInt(HIDDEN_POWER_USED_VAR, 0);
	}

	public synchronized void tryPotenEnchant(int slot)
	{
		Henna henna = get(slot);
		if (henna == null)
		{
			return;
		}

		int step = getHiddenPowerStep();
		PotentialFee fee = HennaHolder.getInstance().getPotentialFee(step);
		if (fee == null)
		{
			owner.sendPacket(new ExNewHennaPotenEnchant(owner, henna, true));
			return;
		}

		int usedPower = owner.getVarInt(HIDDEN_POWER_USED_VAR, 0);
		if (usedPower >= fee.getDailyCount())
		{
			owner.sendPacket(new ExNewHennaPotenEnchant(owner, henna, false));
			return;
		}

		if (fee.getItemId() != 0 && fee.getItemCount() > 0)
		{
			if (!ItemFunctions.deleteItem(owner, fee.getItemId(), fee.getItemCount(), true))
			{
				owner.sendPacket(new ExNewHennaPotenEnchant(owner, henna, true));
				return;
			}
		}

		usedPower++;

		if (usedPower >= fee.getDailyCount() && HennaHolder.getInstance().getPotentialFee(step + 1) != null)
		{
			usedPower = 0;
			owner.setVar(HIDDEN_POWER_STEP_VAR, step + 1, TimeUtils.DAILY_DATE_PATTERN.next(System.currentTimeMillis()));
		}

		owner.setVar(HIDDEN_POWER_USED_VAR, usedPower, TimeUtils.DAILY_DATE_PATTERN.next(System.currentTimeMillis()));

		henna.setEnchantExp(henna.getEnchantExp() + fee.getExpCount());
		henna.updated(true);
		owner.sendPacket(new ExNewHennaPotenEnchant(owner, henna, true));
	}

	@Override
	public String toString()
	{
		return "HennaList[owner=" + owner.getName() + "]";
	}
}
