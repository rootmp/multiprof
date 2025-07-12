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

public class RequestHuntPassReward implements IClientIncomingPacket
{
	private int _huntPassType;
	private boolean _bPremium;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_huntPassType = packet.readC();
		_bPremium = packet.readC() == 1; // bPremium
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

		final ItemData reward;
		final int rewardStep;

		if(_bPremium)
		{
			rewardStep = huntPass.getPremiumRewardStep();
			reward = getReward(HuntPassHolder.getInstance().getPremiumRewards(), rewardStep);
		}
		else
		{
			rewardStep = huntPass.getRewardStep();
			reward = getReward(HuntPassHolder.getInstance().getRewards(), rewardStep);
		}

		if(reward == null)
			return;
		
		//проверка лимита инвентаря 
		if(reward.getId() != 72286)
		{
			final ItemTemplate itemTemplate = ItemHolder.getInstance().getTemplate(reward.getId());
			final long weight = itemTemplate.getWeight() * reward.getCount();
			final long slots = itemTemplate.isStackable() ? 1 : reward.getCount();
			if(!player.getInventory().validateWeight(weight) || !player.getInventory().validateCapacity(slots))
			{
				player.sendPacket(new SystemMessage(SystemMsg.YOUR_INVENTORY_S_WEIGHT_LIMIT_HAS_BEEN_EXCEEDED_SO_YOU_CAN_T_RECEIVE_THE_REWARD_PLEASE_FREE_UP_SOME_SPACE_AND_TRY_AGAIN));
				return;
			}
		}

		if(!_bPremium)
			normalReward(player, reward);
		else
			premiumReward(player, reward);

		huntPass.setRewardAlert(false);
		player.sendPacket(new HuntPassInfo(player, _huntPassType));
		player.sendPacket(new HuntPassSayhasSupportInfo(player));
		player.sendPacket(new HuntPassSimpleInfo(player));
	}

	private ItemData getReward(List<ItemData> rewards, int step)
	{
		return step < rewards.size() ? rewards.get(step) : null;
	}

	private void normalReward(Player player, ItemData reward)
	{
		final HuntPass huntPass = player.getHuntPass();
		final int normalRewardStep = huntPass.getRewardStep();
		huntPass.setRewardStep(normalRewardStep + 1);

		if(reward.getId() == 72286)
		{
			int count = (int) reward.getCount();
			huntPass.addSayhaTime(count);
			player.sendPacket(new SystemMessage(SystemMsg.YOU_RECEIVED_S1_SAYHA_S_GRACE_SUSTENTION_POINTS).addNumber(count));
		}
		else
			ItemFunctions.addItem(player, reward.getId(), reward.getCount());
	}

	private void premiumReward(Player player, ItemData reward)
	{
		final HuntPass huntPass = player.getHuntPass();
		final int rewardStep = huntPass.getPremiumRewardStep();
		if(huntPass.isPremium())
		{
			huntPass.setPremiumRewardStep(rewardStep + 1);
			if(reward.getId() == 72286)
			{
				int count = (int) reward.getCount();
				huntPass.addSayhaTime(count);
				player.sendPacket(new SystemMessage(SystemMsg.YOU_RECEIVED_S1_SAYHA_S_GRACE_SUSTENTION_POINTS).addNumber(count));
			}
			else
				ItemFunctions.addItem(player, reward.getId(), reward.getCount());
		}
	}
}
