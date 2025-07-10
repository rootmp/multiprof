package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.templates.henna.HennaTemplate;

public class HennaItemInfoPacket implements IClientOutgoingPacket
{
	private final int _str, _con, _dex, _int, _wit, _men;
	private final long _adena;
	private final HennaTemplate _hennaTemplate;
	private final boolean _available;

	public HennaItemInfoPacket(HennaTemplate hennaTemplate, Player player)
	{
		_hennaTemplate = hennaTemplate;
		_adena = player.getAdena();
		_str = player.getSTR();
		_dex = player.getDEX();
		_con = player.getCON();
		_int = player.getINT();
		_wit = player.getWIT();
		_men = player.getMEN();
		_available = _hennaTemplate.isForThisClass(player);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_hennaTemplate.getSymbolId()); // symbol Id
		packetWriter.writeD(_hennaTemplate.getDyeId()); // item id of dye
		packetWriter.writeQ(_hennaTemplate.getDrawCount());
		packetWriter.writeQ(_hennaTemplate.getDrawPrice());
		packetWriter.writeD(_available); // able to draw or not 0 is false and 1 is true
		packetWriter.writeQ(_adena);
		packetWriter.writeD(_int); // current INT
		packetWriter.writeH(_int + _hennaTemplate.getStatINT()); // equip INT
		packetWriter.writeD(_str); // current STR
		packetWriter.writeH(_str + _hennaTemplate.getStatSTR()); // equip STR
		packetWriter.writeD(_con); // current CON
		packetWriter.writeH(_con + _hennaTemplate.getStatCON()); // equip CON
		packetWriter.writeD(_men); // current MEM
		packetWriter.writeH(_men + _hennaTemplate.getStatMEN()); // equip MEM
		packetWriter.writeD(_dex); // current DEX
		packetWriter.writeH(_dex + _hennaTemplate.getStatDEX()); // equip DEX
		packetWriter.writeD(_wit); // current WIT
		packetWriter.writeH(_wit + _hennaTemplate.getStatWIT()); // equip WIT
		packetWriter.writeD(0x00); // current LUC
		packetWriter.writeH(0x00); // equip LUC
		packetWriter.writeD(0x00); // current CHA
		packetWriter.writeH(0x00); // equip CHA
		packetWriter.writeD(0x00); // Periodic
	}
}