package l2s.gameserver.taskmanager;

import l2s.gameserver.Config;
import l2s.gameserver.taskmanager.tasks.AntharasLairCloseTask;
import l2s.gameserver.taskmanager.tasks.AntharasLairOpenTask;
import l2s.gameserver.taskmanager.tasks.CheckItemsTask;
import l2s.gameserver.taskmanager.tasks.DailyTask;
import l2s.gameserver.taskmanager.tasks.DeleteExpiredMailTask;
import l2s.gameserver.taskmanager.tasks.FrostLordCastleCloseTask;
import l2s.gameserver.taskmanager.tasks.FrostLordCastleOpenTask;
import l2s.gameserver.taskmanager.tasks.HellboundCloseTask;
import l2s.gameserver.taskmanager.tasks.HellboundOpenTask;
import l2s.gameserver.taskmanager.tasks.MonthlyTask;
import l2s.gameserver.taskmanager.tasks.OlympiadSaveTask;
import l2s.gameserver.taskmanager.tasks.PledgeHuntingSaveTask;
import l2s.gameserver.taskmanager.tasks.RaidBossSaveTask;
import l2s.gameserver.taskmanager.tasks.SubjugationStartTask;
import l2s.gameserver.taskmanager.tasks.SubjugationStopTask;
import l2s.gameserver.taskmanager.tasks.WeeklyTask;

/**
 * @author Bonux
 **/
public class AutomaticTasks
{
	public static void init()
	{
		if (Config.ENABLE_OLYMPIAD)
			new OlympiadSaveTask();

		new DailyTask();
		new DeleteExpiredMailTask();
		new CheckItemsTask();
		new RaidBossSaveTask();
		new WeeklyTask();
		new PledgeHuntingSaveTask();
		new SubjugationStopTask();
		new SubjugationStartTask();
		if (!Config.HELLBOUND_ENABLED_ALL_TIME)
		{
			new HellboundOpenTask();
			new HellboundCloseTask();
		}
		new MonthlyTask();
		new AntharasLairOpenTask();
		new AntharasLairCloseTask();
		new FrostLordCastleOpenTask();
		new FrostLordCastleCloseTask();
	}
}
