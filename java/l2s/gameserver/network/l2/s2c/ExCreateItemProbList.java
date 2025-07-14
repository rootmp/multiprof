package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.item.capsule.CapsuleData;
import l2s.gameserver.templates.item.capsule.CreateItemGroupInfo;
import l2s.gameserver.templates.item.capsule.CreateItemInfo;

public class ExCreateItemProbList implements IClientOutgoingPacket
{
	private CapsuleData _capsule;

	public ExCreateItemProbList(CapsuleData capsule)
	{
		_capsule = capsule;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_capsule.getItemId());

		packetWriter.writeD(_capsule.getCreateItems().size());
		for(CreateItemInfo item : _capsule.getCreateItems())
		{
			packetWriter.writeD(item.getItemId());
			packetWriter.writeD(item.getEnchant());
			packetWriter.writeQ(item.getItemCount());
			packetWriter.writeQ(item.getProb());
		}

		packetWriter.writeD(_capsule.getCreateRandomItems().size());
		for(CreateItemInfo item : _capsule.getCreateRandomItems())
		{
			packetWriter.writeD(item.getItemId());
			packetWriter.writeD(item.getEnchant());
			packetWriter.writeQ(item.getItemCount());
			packetWriter.writeQ(item.getProb());
		}

		packetWriter.writeD(_capsule.getCreateMultiItems().size());
		for(CreateItemGroupInfo group : _capsule.getCreateMultiItems())
		{
			packetWriter.writeD(group.getGroup());
			packetWriter.writeD(group.getCreateItems().size());
			for(CreateItemInfo item : group.getCreateItems())
			{
				packetWriter.writeD(item.getItemId());
				packetWriter.writeD(item.getEnchant());
				packetWriter.writeQ(item.getItemCount());
				packetWriter.writeQ(item.getProb());
			}
		}

		return true;
	}
}