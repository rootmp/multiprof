package handler.bayleeinterface;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.AutoFarm;

public class AutoFarmSettings extends BayleeInterfaceLoader
{
	private static final String[] _commandList = new String[]
	{
		"AUTOFARM_SETTING",
		"AUTOFARM"
	};

	@Override
	public boolean useBayleeInterfaceCommand(String command, Player player, String params)
	{
		if (player == null)
		{
			return false;
		}

		if (command.equalsIgnoreCase(_commandList[0]))
		{
			try
			{
				final String[] split = params.split(";");
				final AutoFarm ap = player.getAutoFarm();
				final boolean showRange = Boolean.parseBoolean(split[0]);
				final boolean fixedFarm = Boolean.parseBoolean(split[1]);
				final boolean targetRaid = Boolean.parseBoolean(split[2]);
				final int shortRange = Integer.parseInt(split[3]);
				final int longRange = Integer.parseInt(split[4]);
				if (ap.isShowAutoFarmRange() != showRange)
				{
					ap.setShowAutoFarmRange(showRange);
				}
				if (ap.isFixedFarmRange() != showRange)
				{
					ap.setFixedFarmRange(fixedFarm);
				}
				if (ap.isTargetRaidBoss() != targetRaid)
				{
					ap.setTargetRaidBoss(targetRaid);
				}
				if (ap.getMeleeFarmDistance() != shortRange)
				{
					if (shortRange < 0 || shortRange > 4)
					{
						player.sendMessage("Invalid range selected.");
						return true;
					}
					else
					{
						ap.setMeleeFarmDistance(shortRange);
						player.sendMessage("Short Target Range set to " + shortRange + ".");
					}
				}
				if (ap.getLongRangeFarmDistance() != longRange)
				{
					if (longRange < 0 || longRange > 4)
					{
						player.sendMessage("Invalid range selected.");
						return true;
					}
					else
					{
						ap.setLongRangeFarmDistance(longRange);
						player.sendMessage("Long Target Range set to " + longRange + ".");
					}
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
		if (command.equalsIgnoreCase(_commandList[1]))
		{
			try
			{
				final String[] split = params.split(";");
				final AutoFarm ap = player.getAutoFarm();
				if (split[0].equals("CENTER"))
				{
					final boolean setting = Boolean.parseBoolean(split[1]);
					if (ap.isFixedFarmRange() != setting)
					{
						ap.setFixedFarmRange(setting);
						player.sendMessage("Auto Farm fixed radius " + String.valueOf(setting ? "enabled" : "disabled") + ".");
					}
					return true;
				}
				if (split[0].contains("SHOW"))
				{
					final boolean setting = Boolean.parseBoolean(split[1]);
					if (ap.isShowAutoFarmRange() != setting)
					{
						ap.setShowAutoFarmRange(setting);
						player.sendMessage("Auto Farm show range " + String.valueOf(setting ? "enabled" : "disabled") + ".");
					}
					return true;
				}
				if (split[0].equals("RAID"))
				{
					final boolean setting = Boolean.parseBoolean(split[1]);
					if (ap.isTargetRaidBoss() != setting)
					{
						ap.setTargetRaidBoss(setting);
						player.sendMessage("Auto Farm target raid boss " + String.valueOf(setting ? "enabled" : "disabled") + ".");
					}
					return true;
				}
				if (split[0].equals("RANGE"))
				{
					if (split.length != 3)
					{
						return false;
					}

					final int shortRange = Integer.parseInt(split[1]);
					final int longRange = Integer.parseInt(split[2]);
					if (ap.getMeleeFarmDistance() != shortRange)
					{
						if (shortRange < 0 || shortRange > 4)
						{
							player.sendMessage("Invalid range selected.");
							return true;
						}
						else
						{
							ap.setMeleeFarmDistance(shortRange);
							player.sendMessage("Short Target Range set to " + shortRange + ".");
						}
					}
					if (ap.getLongRangeFarmDistance() != longRange)
					{
						if (longRange < 0 || longRange > 4)
						{
							player.sendMessage("Invalid range selected.");
							return true;
						}
						else
						{
							ap.setLongRangeFarmDistance(longRange);
							player.sendMessage("Long Target Range set to " + longRange + ".");
						}
					}
					return false;
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public String[] getCommandList()
	{
		return _commandList;
	}
}