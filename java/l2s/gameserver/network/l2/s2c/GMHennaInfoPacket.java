package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.BaseStats;
import l2s.gameserver.templates.item.henna.HennaPoten;

public class GMHennaInfoPacket implements IClientOutgoingPacket
{
	private final Player _player;
	private final List<HennaPoten> _hennas = new ArrayList<>();

	public GMHennaInfoPacket(Player player)
	{
		_player = player;
		for(HennaPoten henna : _player.getHennaPotenList())
			if(henna != null)
				_hennas.add(henna);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_player.getHennaValue(BaseStats.INT)); // equip INT
		packetWriter.writeH(_player.getHennaValue(BaseStats.STR)); // equip STR
		packetWriter.writeH(_player.getHennaValue(BaseStats.CON)); // equip CON
		packetWriter.writeH(_player.getHennaValue(BaseStats.MEN)); // equip MEN
		packetWriter.writeH(_player.getHennaValue(BaseStats.DEX)); // equip DEX
		packetWriter.writeH(_player.getHennaValue(BaseStats.WIT)); // equip WIT
		packetWriter.writeH(0); // equip LUC
		packetWriter.writeH(0); // equip CHA
		packetWriter.writeD(3); // Slots
		packetWriter.writeD(_hennas.size()); // Size
		for(HennaPoten henna : _hennas)
		{
			packetWriter.writeD(henna.getPotenId());
			packetWriter.writeD(1);
		}
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		return true;
	}
}