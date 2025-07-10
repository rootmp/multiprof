package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.henna.HennaTemplate;

public class HennaEquipListPacket implements IClientOutgoingPacket
{
	private final Player _player;
	private final int _emptySlots;
	private final long _adena;
	private final List<HennaTemplate> _hennas = new ArrayList<HennaTemplate>();

	public HennaEquipListPacket(Player player)
	{
		_player = player;
		_adena = player.getAdena();
		_emptySlots = player.getHennaList().getFreeSize();

		for (HennaTemplate element : HennaHolder.getInstance().getHennas())
		{
			if (player.getInventory().getItemByItemId(element.getDyeId()) != null)
				_hennas.add(element);
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_adena);
		packetWriter.writeD(_emptySlots);
		packetWriter.writeD(_hennas.size());
		for (HennaTemplate henna : _hennas)
		{
			packetWriter.writeD(henna.getSymbolId()); // symbolid
			packetWriter.writeD(henna.getDyeId()); // itemid of dye
			packetWriter.writeQ(henna.getDrawCount());
			packetWriter.writeQ(henna.getDrawPrice());
			packetWriter.writeD(henna.isForThisClass(_player) ? 0x01 : 0x00);
		}
	}
}