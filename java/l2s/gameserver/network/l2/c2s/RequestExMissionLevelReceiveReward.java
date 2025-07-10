package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import java.util.Calendar;

import l2s.gameserver.data.xml.holder.MissionLevelRewardsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.MissionLevelReward;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExMissionLevelRewardList;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.dailymissions.MissionLevelRewardTemplate;
import l2s.gameserver.templates.item.data.MissionLevelRewardData;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public class RequestExMissionLevelReceiveReward implements IClientIncomingPacket
{
	private int _level;
	private int _rewardType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_level = packet.readD();
		_rewardType = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}

		MissionLevelReward info = player.getMissionLevelReward();
		int currentLvl = info.getLevel();
		int lastTakenBasic = info.lastTakenBasic();
		int lastTakenAdditional = info.lastTakenAdditional();
		int lastTakenBonus = info.lastTakenBonus();
		boolean takenFinal = info.takenFinal();

		if (currentLvl < _level)
		{
			sendPacket(new SystemMessagePacket(SystemMsg.SYSTEM_ERROR_PLEASE_REFRESH_AND_TRY_AGAIN));
			return;
		}

		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		MissionLevelRewardTemplate template = MissionLevelRewardsHolder.getInstance().getRewardsInfo(month);
		MissionLevelRewardData data = template.getRewards().get(_level - 1);

		switch (_rewardType)
		{
			case 1:
			{
				if (lastTakenBasic >= _level)
				{
					sendPacket(new SystemMessagePacket(SystemMsg.SYSTEM_ERROR_PLEASE_REFRESH_AND_TRY_AGAIN));
					return;
				}
				else
				{
					player.getMissionLevelReward().setLastTakenBasic(_level);
					ItemFunctions.addItem(player, data.getId(), data.getCount());
				}
				break;
			}
			case 2:
			{
				if (lastTakenAdditional >= _level)
				{
					sendPacket(new SystemMessagePacket(SystemMsg.SYSTEM_ERROR_PLEASE_REFRESH_AND_TRY_AGAIN));
					return;
				}
				else
				{
					if ((data.getAdditionalReward().getId() == 0) || (data.getAdditionalReward().getCount() == 0))
					{
						sendPacket(new SystemMessagePacket(SystemMsg.SYSTEM_ERROR_PLEASE_REFRESH_AND_TRY_AGAIN));
						return;
					}
					else
					{
						player.getMissionLevelReward().setLastTakenAdditional(_level);
						ItemFunctions.addItem(player, data.getAdditionalReward().getId(), data.getAdditionalReward().getCount());
					}
				}
				break;
			}
			case 3:
			{
				boolean canTake = _level >= template.getMaxRewardLvl() ? true : false;
				if (takenFinal || !canTake)
				{
					sendPacket(new SystemMessagePacket(SystemMsg.SYSTEM_ERROR_PLEASE_REFRESH_AND_TRY_AGAIN));
					return;
				}
				else
				{
					player.getMissionLevelReward().setTakenFinal(true);
					ItemFunctions.addItem(player, template.getFinalReward().getId(), template.getFinalReward().getCount());
				}
				break;
			}
			case 4:
			{
				if (lastTakenBonus >= _level)
				{
					sendPacket(new SystemMessagePacket(SystemMsg.SYSTEM_ERROR_PLEASE_REFRESH_AND_TRY_AGAIN));
					return;
				}
				else
				{
					player.getMissionLevelReward().setLastTakenBonus(_level);
					ItemFunctions.addItem(player, data.getId(), data.getCount());
				}
				break;
			}
		}

		sendPacket(new ExMissionLevelRewardList(player));
	}
}