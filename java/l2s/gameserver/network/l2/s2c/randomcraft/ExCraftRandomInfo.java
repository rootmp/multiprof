package l2s.gameserver.network.l2.s2c.randomcraft;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.randomCraft.RandomCraftRewardItem;

public class ExCraftRandomInfo implements IClientOutgoingPacket
{
	private final Player _player;
	
	public ExCraftRandomInfo(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		final List<RandomCraftRewardItem> rewards = _player.getRandomCraft().getRewards();
		int size = 5;
		packetWriter.writeD(size); // size
		for (int i = 0; i < rewards.size(); i++)
		{
			final RandomCraftRewardItem holder = rewards.get(i);
			if ((holder != null) && (holder.getItemId() != 0))
			{
				packetWriter.writeC(holder.isLocked() ? 1 : 0); // Locked
				packetWriter.writeD(holder.getLockLeft()); // Rolls it will stay locked
				packetWriter.writeD(holder.getItemId()); // Item id
				packetWriter.writeQ(holder.getItemCount()); // Item count
			}
			else
			{
				packetWriter.writeC(0);
				packetWriter.writeD(0);
				packetWriter.writeD(0);
				packetWriter.writeQ(0);
			}
			size--;
		}
		// Write missing
		for (int i = size; i > 0; i--)
		{
			packetWriter.writeC(0);
			packetWriter.writeD(0);
			packetWriter.writeD(0);
			packetWriter.writeQ(0);
		}
		return true;
	}
}
