package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.model.actor.instances.player.HennaList;

public class HennaInfoPacket implements IClientOutgoingPacket
{
	private final Player _player;
	private final HennaList _hennaList;

	public HennaInfoPacket(Player player)
	{
		_player = player;
		_hennaList = player.getHennaList();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_hennaList.getINT()); // equip INT
		packetWriter.writeH(_hennaList.getSTR()); // equip STR
		packetWriter.writeH(_hennaList.getCON()); // equip CON
		packetWriter.writeH(_hennaList.getMEN()); // equip MEN
		packetWriter.writeH(_hennaList.getDEX()); // equip DEX
		packetWriter.writeH(_hennaList.getWIT()); // equip WIT
		packetWriter.writeH(0x00); // LUC
		packetWriter.writeH(0x00); // CHA
		packetWriter.writeD(HennaList.MAX_SIZE); // interlude, slots?
		packetWriter.writeD(_hennaList.size());
		for (Henna henna : _hennaList.values())
		{
			packetWriter.writeD(henna.getId());
			packetWriter.writeD(_hennaList.isActive(henna));
		}

		packetWriter.writeD(0x00); // Premium symbol ID
		packetWriter.writeD(0x00); // Premium symbol left time
		packetWriter.writeD(0x00); // Premium symbol active
	}
}