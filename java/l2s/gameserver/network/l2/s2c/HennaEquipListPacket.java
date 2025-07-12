package l2s.gameserver.network.l2.s2c;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.dataparser.data.holder.DyeDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.henna.Henna;

public class HennaEquipListPacket implements IClientOutgoingPacket
{
	private final Player _player;
	private final List<Henna> _hennaEquipList;

	public HennaEquipListPacket(Player player)
	{
		_player = player;
		_hennaEquipList = DyeDataHolder.getInstance().getHennaList(player);
	}

	public HennaEquipListPacket(Player player, List<Henna> list)
	{
		_player = player;
		_hennaEquipList = list;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_player.getAdena());
		packetWriter.writeD(3);
		packetWriter.writeD(_hennaEquipList.size());
		for(Henna henna : _hennaEquipList)
		{
			packetWriter.writeD(henna.getDyeId());
			packetWriter.writeD(henna.getDyeItemId());
			packetWriter.writeQ(henna.getNeedCount());//unk
			packetWriter.writeD(1);//unk2
		}
		return true;
	}
}
