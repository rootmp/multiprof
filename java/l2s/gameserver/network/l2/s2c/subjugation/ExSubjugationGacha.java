package l2s.gameserver.network.l2.s2c.subjugation;

import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.SubjugationsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.SubjugationTemplate;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public class ExSubjugationGacha implements IClientOutgoingPacket
{
	int _zoneId, _count;
	Player _player;

	public ExSubjugationGacha(Player player, int zoneId, int keysCount)
	{
		_player = player;
		_zoneId = zoneId;
		_count = keysCount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		_player.reduceAdena(20_000 * _count, true);
		SubjugationTemplate temp = SubjugationsHolder.getInstance().getFields().get(_zoneId);
		int[] rewardCount = new int[6];
		for (int i = 0; i < _count; i++)
		{
			int chance = Rnd.get(10000);
			if (chance > 4000)
				rewardCount[0]++;
			else if (chance > 1000)
				rewardCount[1]++;
			else if (chance > 500)
				rewardCount[2]++;
			else if (chance > 300)
				rewardCount[3]++;
			else if (chance > 50)
				rewardCount[4]++;
			else if (chance > 25)
				rewardCount[5]++;
		}

		packetWriter.writeD(_count);
		if (rewardCount[0] > 0)
		{
			packetWriter.writeD(temp.getRewardItems()[0].getId());
			packetWriter.writeD(rewardCount[0]);
			ItemFunctions.addItem(_player, temp.getRewardItems()[0].getId(), rewardCount[0], true);

		}
		if (rewardCount[1] > 0)
		{
			packetWriter.writeD(temp.getRewardItems()[1].getId());
			packetWriter.writeD(rewardCount[1]);
			ItemFunctions.addItem(_player, temp.getRewardItems()[1].getId(), rewardCount[1], true);
		}
		if (rewardCount[2] > 0)
		{
			packetWriter.writeD(temp.getRewardItems()[2].getId());
			packetWriter.writeD(rewardCount[2]);
			ItemFunctions.addItem(_player, temp.getRewardItems()[2].getId(), rewardCount[2], true);
		}
		if (rewardCount[3] > 0)
		{
			packetWriter.writeD(temp.getRewardItems()[3].getId());
			packetWriter.writeD(rewardCount[3]);
			ItemFunctions.addItem(_player, temp.getRewardItems()[3].getId(), rewardCount[3], true);
		}
		if (rewardCount[4] > 0)
		{
			packetWriter.writeD(temp.getRewardItems()[4].getId());
			packetWriter.writeD(rewardCount[4]);
			ItemFunctions.addItem(_player, temp.getRewardItems()[4].getId(), rewardCount[4], true);
		}
		if (rewardCount[5] > 0)
		{
			packetWriter.writeD(temp.getRewardItems()[5].getId());
			packetWriter.writeD(rewardCount[5]);
			ItemFunctions.addItem(_player, temp.getRewardItems()[5].getId(), rewardCount[5], true);
		}
		return true;
	}
}