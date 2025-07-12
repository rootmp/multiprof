package l2s.gameserver.network.l2.c2s.huntpass;

import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.HuntPassHolder;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.HuntPass;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassInfo;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassSayhasSupportInfo;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassSimpleInfo;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.utils.ItemFunctions;

public class RequestHuntPassRewardAll implements IClientIncomingPacket
{
	private int _huntPassType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_huntPassType = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		final HuntPass huntPass = player.getHuntPass();
		if(huntPass == null)
			return;

		final List<ItemData> rewards = HuntPassHolder.getInstance().getRewards();
		final List<ItemData> premiumRewards = HuntPassHolder.getInstance().getPremiumRewards();

		// Handle normal rewards
		while(huntPass.getCurrentStep() > huntPass.getRewardStep())
		{
			int rewardStep = huntPass.getRewardStep();
			if(rewardStep >= rewards.size())
				break;

			final ItemData reward = rewards.get(rewardStep);
			if(!validateAndProcessReward(player, reward, "HuntPassRewardAll"))
				return;

			huntPass.setRewardStep(rewardStep + 1);
		}
		// Handle premium rewards
		if(huntPass.isPremium())
		{

			while(huntPass.getCurrentStep() > huntPass.getPremiumRewardStep())
			{
				int rewardStep = huntPass.getPremiumRewardStep();
				if(rewardStep >= premiumRewards.size())
					break;

				final ItemData premiumReward = premiumRewards.get(rewardStep);
				if(!validateAndProcessReward(player, premiumReward, "HuntPassPremiumRewardAll"))
					return;

				huntPass.setPremiumRewardStep(rewardStep + 1);
			}
		}

		huntPass.setRewardAlert(false);
		player.sendPacket(new HuntPassInfo(player, _huntPassType));
		player.sendPacket(new HuntPassSayhasSupportInfo(player));
		player.sendPacket(new HuntPassSimpleInfo(player));
	}

	private boolean validateAndProcessReward(Player player, ItemData reward, String source)
	{
		final ItemTemplate itemTemplate = ItemHolder.getInstance().getTemplate(reward.getId());
		final long weight = itemTemplate.getWeight() * reward.getCount();
		final long slots = itemTemplate.isStackable() ? 1 : reward.getCount();
		if(!player.getInventory().validateWeight(weight) || !player.getInventory().validateCapacity(slots))
		{
			player.sendPacket(new SystemMessage(SystemMsg.YOUR_INVENTORY_S_WEIGHT_LIMIT_HAS_BEEN_EXCEEDED_SO_YOU_CAN_T_RECEIVE_THE_REWARD_PLEASE_FREE_UP_SOME_SPACE_AND_TRY_AGAIN));
			return false;
		}

		if(reward.getId() == 72286)
		{
			int count = (int) reward.getCount();
			player.getHuntPass().addSayhaTime(count);
			final SystemMessage msg = new SystemMessage(SystemMsg.YOU_RECEIVED_S1_SAYHA_S_GRACE_SUSTENTION_POINTS);
			msg.addNumber(count);
			player.sendPacket(msg);
		}
		else
		{
			ItemFunctions.addItem(player, reward.getId(), reward.getCount());
		}
		return true;
	}
}
