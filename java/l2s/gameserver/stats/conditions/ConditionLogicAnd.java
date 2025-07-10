package l2s.gameserver.stats.conditions;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.entity.events.Event;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.SkillInfo;
import l2s.gameserver.stats.Env;

public class ConditionLogicAnd extends Condition
{
	private final static Condition[] emptyConditions = new Condition[0];

	public Condition[] _conditions = emptyConditions;

	public ConditionLogicAnd()
	{
		super();
	}

	public void add(Condition condition)
	{
		if (condition == null)
		{
			return;
		}

		final int len = _conditions.length;
		final Condition[] tmp = new Condition[len + 1];
		System.arraycopy(_conditions, 0, tmp, 0, len);
		tmp[len] = condition;
		_conditions = tmp;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		for (Condition c : _conditions)
		{
			if (!c.test(env))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Condition for PetEvolve
	 * 
	 * @param character
	 * @param aimingTarget
	 * @param condTarget
	 * @param owner
	 * @param value
	 * @param forceUse
	 * @param sendMsg
	 * @param autoUse
	 * @return
	 */
	public final boolean checkCondition(Creature character, Creature aimingTarget, Creature condTarget, Object owner, double value, boolean forceUse, boolean sendMsg, boolean autoUse)
	{
		if (character != null)
		{
			for (Event event : character.getEvents())
			{
				if (!event.checkCondition(character, getClass()))
				{
					return false;
				}
			}
			if (!testImpl(new Env(character, aimingTarget, null)))
			{
				if (sendMsg)
				{
					SystemMsg msg = _message;
					if (msg != null)
					{
						if (msg.size() > 0)
						{
							if (owner instanceof SkillInfo)
							{
								character.sendPacket((new SystemMessagePacket(msg)).addSkillName((SkillInfo) owner));
							}
							if (owner instanceof ItemInstance)
							{
								character.sendPacket((new SystemMessagePacket(msg)).addItemName(((ItemInstance) owner).getItemId()));
							}
						}
						else
						{
							character.sendPacket(msg);
						}
					}
				}
				return false;
			}
		}
		return true;
	}
}
