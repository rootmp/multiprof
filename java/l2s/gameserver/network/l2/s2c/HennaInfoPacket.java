package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.BaseStats;
import l2s.gameserver.templates.item.henna.Henna;
import l2s.gameserver.templates.item.henna.HennaPoten;

/**
 * This server packet sends the player's henna information.
 * @author Zoey76
 */
public class HennaInfoPacket implements IClientOutgoingPacket
{
	private final Player _player;
	private final List<Henna> _hennas = new ArrayList<>();

	public HennaInfoPacket(Player player)
	{
		_player = player;
		for(HennaPoten hennaPoten : _player.getHennaPotenList())
		{
			final Henna henna = hennaPoten.getHenna();
			if(henna != null)
			{
				_hennas.add(henna);
			}
		}
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
		packetWriter.writeD(3 - _player.getHennaEmptySlots()); // Slots
		packetWriter.writeD(_hennas.size()); // Size
		for(Henna henna : _hennas)
		{
			packetWriter.writeD(henna.getDyeId());
			packetWriter.writeD(henna.isAllowedClass(_player) ? 1 : 0);
		}
		packetWriter.writeD(0); // Premium Slot Dye ID
		packetWriter.writeD(0); // Premium Slot Dye Time Left
		packetWriter.writeD(0); // Premium Slot Dye ID isValid
		return true;
	}
}
