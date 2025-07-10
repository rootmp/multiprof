package handler.dailymissions;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.listener.CharListener;
import l2s.gameserver.listener.actor.OnKillListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;

public class ClanArenaWeek extends ProgressDailyMissionHandler
{
	private final int[] MONSTER_IDS =
	{
		25794,
		25795,
		25796,
		25797,
		25798,
		25799,
		25800,
		25801,
		25802,
		25803,
		25804,
		25805,
		25806,
		25807,
		25808,
		25809,
		25810,
		25811,
		25812,
		25813,
		25834,
		25835,
		25836,
		25837,
		25838
	};
	private static final SchedulingPattern REUSE_PATTERN = new SchedulingPattern("30 6 * * 1");

	private class HandlerListeners implements OnKillListener
	{
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			Player player = actor.getPlayer();
			if (player != null)
			{
				for (int mob : MONSTER_IDS)
				{
					if (victim.getNpcId() == mob)
					{
						progressMission(player, 1, true, victim.getLevel());
					}
				}
			}
		}

		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
	}

	private final HandlerListeners _handlerListeners = new HandlerListeners();

	@Override
	public CharListener getListener()
	{
		return _handlerListeners;
	}

	@Override
	public SchedulingPattern getReusePattern()
	{
		return REUSE_PATTERN;
	}

}
