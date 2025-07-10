package l2s.gameserver.taskmanager.tasks;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExNoticePostArrived;
import l2s.gameserver.network.l2.s2c.ExUnReadMailCount;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 **/
public class SubjugationStopTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(SubjugationStopTask.class);
	private static final int CRUMA_TOWER_PURGE_RANK_REWARD = 95460;
	private static final int SILENT_VALLEY_PURGE_RANK_REWARD = 95461;
	private static final int LIZARDMAN_PLAINS_PURGE_RANK_REWARD = 95463;
	private static final int TOWER_OF_INSOLENCE_PURGE_RANK_REWARD = 95462;
	private static final int DRAGON_VALLEY_PURGE_RANK_REWARD = 95464;
	private static final int SEL_MAHUM_PURGE_RANK_REWARD = 95465;
	private static final int ORC_BARRACKS_PURGE_RANK_REWARD = 95466;
	private static final int GIANTS_CAVE_PURGE_RANK_REWARD = 96724;
	private static final int GODDARD_PURGE_RANK_REWARD = 97225;

	private static final SchedulingPattern PATTERN = new SchedulingPattern("59 23 * * sun");

	public SubjugationStopTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Subjugation Stop Global Task: launched.");
		Config.SUBJUGATION_ENABLED = false;
		for (int i = 1; i < 8; i++)
		{
			Map<Integer, StatsSet> ranking = RankManager.getInstance().getSubjugationRanks(i);
			int count = 5;
			for (int id : ranking.keySet())
			{
				StatsSet player = ranking.get(id);
				Mail mail = new Mail();
				mail.setSenderId(1);
				mail.setSenderName("Subjugation System");
				mail.setReceiverId(player.getInteger("charId"));
				mail.setReceiverName(player.getString("name"));
				mail.setTopic("Subjugation Reward");
				mail.setBody("Subjugation Reward to top5 rankers");

				ItemInstance item = null;
				if (i == 1)
				{
					item = ItemFunctions.createItem(CRUMA_TOWER_PURGE_RANK_REWARD);
				}
				else if (i == 2)
				{
					item = ItemFunctions.createItem(SILENT_VALLEY_PURGE_RANK_REWARD);
				}
				else if (i == 3)
				{
					item = ItemFunctions.createItem(LIZARDMAN_PLAINS_PURGE_RANK_REWARD);
				}
				else if (i == 4)
				{
					item = ItemFunctions.createItem(TOWER_OF_INSOLENCE_PURGE_RANK_REWARD);
				}
				else if (i == 5)
				{
					item = ItemFunctions.createItem(DRAGON_VALLEY_PURGE_RANK_REWARD);
				}
				else if (i == 6)
				{
					item = ItemFunctions.createItem(SEL_MAHUM_PURGE_RANK_REWARD);
				}
				else if (i == 7)
				{
					item = ItemFunctions.createItem(ORC_BARRACKS_PURGE_RANK_REWARD);
				}
				else if (i == 8)
				{
					item = ItemFunctions.createItem(GIANTS_CAVE_PURGE_RANK_REWARD);
				}
				else if (i == 9)
				{
					item = ItemFunctions.createItem(GODDARD_PURGE_RANK_REWARD);
				}
				if (item != null)
				{
					item.setLocation(ItemLocation.MAIL);
					item.setCount(count);
					count--;
					item.save();

					mail.addAttachment(item);
					mail.setType(Mail.SenderType.BIRTHDAY);
					mail.setUnread(true);
					mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
					mail.save();

					Player plr = GameObjectsStorage.getPlayer(player.getInteger("charId"));
					if (plr != null)
					{
						plr.sendPacket(ExNoticePostArrived.STATIC_TRUE);
						plr.sendPacket(new ExUnReadMailCount(plr));
						plr.sendPacket(SystemMsg.THE_MAIL_HAS_ARRIVED);
					}
				}
			}
		}

		_log.info("Subjugation Stop Global Task: completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}