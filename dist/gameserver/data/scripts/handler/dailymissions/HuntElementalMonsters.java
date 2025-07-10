package handler.dailymissions;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.listener.CharListener;
import l2s.gameserver.listener.actor.OnKillListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ElementalElement;

/**
 * @author Bonux
 **/
public class HuntElementalMonsters extends ProgressDailyMissionHandler
{
	private class HandlerListeners implements OnKillListener
	{
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			Player player = actor.getPlayer();
			if (player != null)
			{
				if (victim.isMonster() && victim.getActiveElement() != ElementalElement.NONE)
					progressMission(player, 1, true, victim.getLevel());
			}
		}

		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
	}

	private static final SchedulingPattern REUSE_PATTERN = new SchedulingPattern("30 6 * * 1");

	private final HandlerListeners _handlerListeners = new HandlerListeners();

	@Override
	public SchedulingPattern getReusePattern()
	{
		return REUSE_PATTERN;
	}

	@Override
	public CharListener getListener()
	{
		return _handlerListeners;
	}
}
