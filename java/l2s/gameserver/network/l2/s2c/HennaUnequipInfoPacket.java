package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.templates.henna.HennaTemplate;

public class HennaUnequipInfoPacket implements IClientOutgoingPacket
{
	private final HennaTemplate _hennaTemplate;
	private final Player _player;

	public HennaUnequipInfoPacket(HennaTemplate hennaTemplate, Player player)
	{
		_hennaTemplate = hennaTemplate;
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_hennaTemplate.getSymbolId()); // symbol Id
		packetWriter.writeD(_hennaTemplate.getDyeId()); // item id of dye
		packetWriter.writeQ(_hennaTemplate.getRemoveCount());
		packetWriter.writeQ(_hennaTemplate.getRemovePrice());
		packetWriter.writeD(_hennaTemplate.isForThisClass(_player)); // able to draw or not 0 is false and 1 is true
		packetWriter.writeQ(_player.getAdena());
		packetWriter.writeD(_player.getINT()); // current INT
		packetWriter.writeH(_player.getINT() - _hennaTemplate.getStatINT()); // equip INT
		packetWriter.writeD(_player.getSTR()); // current STR
		packetWriter.writeH(_player.getSTR() - _hennaTemplate.getStatSTR()); // equip STR
		packetWriter.writeD(_player.getCON()); // current CON
		packetWriter.writeH(_player.getCON() - _hennaTemplate.getStatCON()); // equip CON
		packetWriter.writeD(_player.getMEN()); // current MEM
		packetWriter.writeH(_player.getMEN() - _hennaTemplate.getStatMEN()); // equip MEM
		packetWriter.writeD(_player.getDEX()); // current DEX
		packetWriter.writeH(_player.getDEX() - _hennaTemplate.getStatDEX()); // equip DEX
		packetWriter.writeD(_player.getWIT()); // current WIT
		packetWriter.writeH(_player.getWIT() - _hennaTemplate.getStatWIT()); // equip WIT
		packetWriter.writeD(0x00); // current LUC
		packetWriter.writeH(0x00); // equip LUC
		packetWriter.writeD(0x00); // current CHA
		packetWriter.writeH(0x00); // equip CHA
		packetWriter.writeD(0x00); // Period
	}
}