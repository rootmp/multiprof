package l2s.gameserver.utils;

import org.napile.primitive.maps.IntIntMap;
import org.napile.primitive.maps.impl.HashIntIntMap;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;

/**
 * @author Bonux
 **/
public class PledgeBonusUtils
{
	public static final int MAX_ATTENDANCE_PROGRESS = 28;
	public static final int MAX_HUNTING_PROGRESS = 13068000;

	public static final IntIntMap ATTENDANCE_REWARDS = new HashIntIntMap(4);
	static
	{
		ATTENDANCE_REWARDS.put(1, Config.CLAN_ATTENDANCE_REWARD_1); // Clan Teamwork - Stage 1
		ATTENDANCE_REWARDS.put(2, Config.CLAN_ATTENDANCE_REWARD_2); // Clan Teamwork - Stage 2
		ATTENDANCE_REWARDS.put(3, Config.CLAN_ATTENDANCE_REWARD_3); // Clan Teamwork - Stage 3
		ATTENDANCE_REWARDS.put(4, Config.CLAN_ATTENDANCE_REWARD_4); // Clan Teamwork - Stage 4
	};

	public static IntIntMap HUNTING_REWARDS = new HashIntIntMap(4);
	static
	{
		HUNTING_REWARDS.put(1, Config.CLAN_HUNTING_REWARD_1); // Supply Box (Standard)
		HUNTING_REWARDS.put(2, Config.CLAN_HUNTING_REWARD_2); // Supply Box (Mid-grade)
		HUNTING_REWARDS.put(3, Config.CLAN_HUNTING_REWARD_3); // Supply Box (High-grade)
		HUNTING_REWARDS.put(4, Config.CLAN_HUNTING_REWARD_4); // Supply Box (Top-grade)
	};

	private static final String ATTENDANCE_REWARD_RECEIVED_VAR = "attendance_reward_received";
	private static final String HUNTING_REWARD_RECEIVED_VAR = "hunting_reward_received";

	public static int getAttendanceProgressLevel(int progress)
	{
		int level = 0;

		int temp = MAX_ATTENDANCE_PROGRESS / 4;
		if (progress >= temp * 4)
			level = 4;
		else if (progress >= temp * 3)
			level = 3;
		else if (progress >= temp * 2)
			level = 2;
		else if (progress >= temp * 1)
			level = 1;

		return level;
	}

	public static int getHuntingProgressLevel(int progress)
	{
		int level = 0;

		int temp = MAX_HUNTING_PROGRESS / 4;
		if (progress >= temp * 4)
			level = 4;
		else if (progress >= temp * 3)
			level = 3;
		else if (progress >= temp * 2)
			level = 2;
		else if (progress >= temp * 1)
			level = 1;

		return level;
	}

	public static boolean isAttendanceRewardAvailable(Player player)
	{
		return !player.getVarBoolean(ATTENDANCE_REWARD_RECEIVED_VAR, false);
	}

	public static boolean isHuntingRewardAvailable(Player player)
	{
		return !player.getVarBoolean(HUNTING_REWARD_RECEIVED_VAR, false);
	}

	public static boolean tryReceiveReward(int type, Player player)
	{
		Clan clan = player.getClan();
		if (clan == null)
			return false;

		switch (type)
		{
			case 0:
			{
				if (!isAttendanceRewardAvailable(player))
					return false;

				int rewardId = ATTENDANCE_REWARDS.get(clan.getYesterdayAttendanceReward());
				if (rewardId <= 0)
					return false;

				SkillEntry skill = SkillEntry.makeSkillEntry(SkillEntryType.NONE, rewardId, 1);
				if (skill != null)
					skill.getEffects(player, player);

				player.setVar(ATTENDANCE_REWARD_RECEIVED_VAR, true, TimeUtils.DAILY_DATE_PATTERN.next(System.currentTimeMillis()));
				return true;
			}
			case 1:
				if (!isHuntingRewardAvailable(player))
					return false;

				int rewardId = HUNTING_REWARDS.get(clan.getYesterdayHuntingReward());
				if (rewardId <= 0)
					return false;

				ItemFunctions.addItem(player, rewardId, 1, true);

				player.setVar(HUNTING_REWARD_RECEIVED_VAR, true, TimeUtils.DAILY_DATE_PATTERN.next(System.currentTimeMillis()));
				return true;
		}
		return false;
	}
}