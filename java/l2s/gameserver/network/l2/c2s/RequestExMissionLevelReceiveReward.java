package l2s.gameserver.network.l2.c2s;

import java.util.Calendar;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.MissionLevelRewardsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.MissionLevelReward;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExMissionLevelRewardList;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.dailymissions.MissionLevelRewardTemplate;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.item.data.MissionLevelRewardData;
import l2s.gameserver.utils.ItemFunctions;

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
		if(player == null)
			return;

		MissionLevelReward info = player.getMissionLevelReward();
		int currentLvl = info.getLevel();
		if(currentLvl < _level)
		{
			sendError(player);
			return;
		}

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		MissionLevelRewardTemplate template = MissionLevelRewardsHolder.getInstance().getRewardsInfo(month, year);

		if(_level > template.getRewards().size() && _rewardType == 4)
			// Если уровень превышает количество стандартных наград, обрабатываем как бонусную награду
			handlePostFinalBonusReward(player, info, _level, template);
		else
		{
			MissionLevelRewardData data = template.getRewards().get(_level - 1);

			switch(_rewardType)
			{
				case 1:
					handleBasicReward(player, info, data);
					break;
				case 2:
					handleAdditionalReward(player, info, data);
					break;
				case 3:
					handleFinalReward(player, info, template);
					break;
				case 4:
					handlePostFinalBonusReward(player, info, _level, template);
					break;
				default:
					sendError(player);
					return;
			}
		}

		player.sendPacket(new ExMissionLevelRewardList(player, month, template));
	}

	private void sendError(Player player)
	{
		player.sendPacket(new SystemMessagePacket(SystemMsg.SYSTEM_ERROR_PLEASE_REFRESH_AND_TRY_AGAIN));
	}

	private void handleBasicReward(Player player, MissionLevelReward info, MissionLevelRewardData data)
	{
		if(info.lastTakenBasic() >= _level)
		{
			sendError(player);
		}
		else
		{
			info.setLastTakenBasic(_level);
			ItemFunctions.addItem(player, data.getId(), data.getCount());
		}
	}

	private void handleAdditionalReward(Player player, MissionLevelReward info, MissionLevelRewardData data)
	{
		if(info.lastTakenAdditional() >= _level || data.getAdditionalReward().getId() == 0 || data.getAdditionalReward().getCount() == 0)
		{
			sendError(player);
		}
		else
		{
			info.setLastTakenAdditional(_level);
			ItemFunctions.addItem(player, data.getAdditionalReward().getId(), data.getAdditionalReward().getCount());
		}
	}

	private void handleFinalReward(Player player, MissionLevelReward info, MissionLevelRewardTemplate template)
	{
		boolean canTake = _level == template.getMaxRewardLvl();
		if(info.takenFinal() || !canTake)
			sendError(player);
		else
		{
			info.setTakenFinal(true);
			ItemFunctions.addItem(player, template.getFinalReward().getId(), template.getFinalReward().getCount());
		}
	}

	private void handlePostFinalBonusReward(Player player, MissionLevelReward info, int level, MissionLevelRewardTemplate template)
	{
		if(info.lastTakenBonus() >= level)
		{
			player.sendPacket(new SystemMessagePacket(SystemMsg.S_13432));
		}
		else
		{
			ItemData bonusReward = template.getBonusReward();
			if(bonusReward != null)
			{
				info.setLastTakenBonus(level);
				ItemFunctions.addItem(player, bonusReward.getId(), bonusReward.getCount());
			}
		}
	}
}
