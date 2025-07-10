package l2s.gameserver.network.l2.s2c;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;

import l2s.gameserver.data.xml.holder.MissionLevelRewardsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.MissionLevelReward;
import l2s.gameserver.templates.dailymissions.MissionLevelRewardTemplate;
import l2s.gameserver.templates.item.data.MissionLevelRewardData;

/**
 * @author nexvill
 */
public class ExMissionLevelRewardList extends L2GameServerPacket
{
	private final Player _player;

	public ExMissionLevelRewardList(Player player)
	{
		_player = player;
	}

	@Override
	protected void writeImpl()
	{
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		MissionLevelRewardTemplate template = MissionLevelRewardsHolder.getInstance().getRewardsInfo(month);
		int maxRewardLvl = template.getMaxRewardLvl();
		int size = maxRewardLvl + template.additionalRewardsSize() + 11;
		MissionLevelReward info = _player.getMissionLevelReward();
		int currentLvl = info.getLevel();
		double points = info.getPoints();
		int lastTakenBasic = info.lastTakenBasic();
		int lastTakenAdditional = info.lastTakenAdditional();
		int lastTakenBonus = info.lastTakenBonus();
		boolean takenFinal = info.takenFinal();

		int totalAvailable = 0;
		int extraAvailable = 0;

		writeD(size); // rewards size

		int i = 1;
		while (i <= lastTakenBasic)
		{
			writeD(1); // type
			writeD(i); // level
			writeD(2); // state (0 - cannot received; 1 - can received; 2 - received)

			i++;
		}
		while (i <= maxRewardLvl)
		{
			if (currentLvl >= i)
			{
				writeD(1);
				writeD(i);
				writeD(1);
				totalAvailable++;
			}
			else
			{
				writeD(1);
				writeD(i);
				writeD(0);
			}
			i++;
		}
		// add reward
		i = 1;
		while (i <= lastTakenAdditional)
		{
			MissionLevelRewardData data = template.getRewards().get(i - 1);
			if (data.getAdditionalReward().getId() != 0)
			{
				writeD(2);
				writeD(i);
				writeD(2);
			}
			i++;
		}
		while (i <= maxRewardLvl)
		{
			MissionLevelRewardData data = template.getRewards().get(i - 1);
			if (currentLvl >= i)
			{
				if (data.getAdditionalReward().getId() != 0)
				{
					writeD(2);
					writeD(i);
					writeD(1);
					totalAvailable++;
					extraAvailable++;
				}
			}
			else
			{
				if (data.getAdditionalReward().getId() != 0)
				{
					writeD(2);
					writeD(i);
					writeD(0);
				}
			}
			i++;
		}
		// final reward
		if (currentLvl < maxRewardLvl)
		{
			writeD(3);
			writeD(20);
			writeD(0);
		}
		else
		{
			if (!takenFinal)
			{
				writeD(3);
				writeD(20);
				writeD(1);
				totalAvailable++;
				extraAvailable++;
			}
			else
			{
				writeD(3);
				writeD(20);
				writeD(2);
			}
		}
		// bonus reward
		i = 21;
		while (i <= lastTakenBonus)
		{
			writeD(4);
			writeD(i);
			writeD(2);
			i++;
		}
		while (i <= 30)
		{
			if (currentLvl >= i)
			{
				writeD(4);
				writeD(i);
				writeD(1);
				totalAvailable++;
				extraAvailable++;
			}
			else
			{
				writeD(4);
				writeD(i);
				writeD(0);
			}
			i++;
		}

		MissionLevelRewardData data = template.getRewards().get(currentLvl);
		double percents = (points / data.getValue()) * 100;
		LocalDateTime nextChange;
		long seasonEnd;
		if (month == 12)
		{
			nextChange = LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR) + 1, Month.JANUARY, 1, 6, 30);
		}
		else
		{
			nextChange = LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR), month + 1, 1, 6, 30);
		}

		seasonEnd = nextChange.atZone(ZoneId.systemDefault()).toEpochSecond() - (System.currentTimeMillis() / 1000);

		writeD(currentLvl); // current level
		writeD((int) percents); // current percents on level
		writeD(Calendar.getInstance().get(Calendar.YEAR)); // season year
		writeD(month); // season month
		writeD(totalAvailable); // total rewards available
		writeD(extraAvailable); // extra rewards available
		writeD((int) seasonEnd); // remain season time
	}
}
