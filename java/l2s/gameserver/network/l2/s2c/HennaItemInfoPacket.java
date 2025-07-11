package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.BaseStats;
import l2s.gameserver.templates.item.henna.Henna;

/**
 * @author Zoey76
 */
public class HennaItemInfoPacket implements IClientOutgoingPacket
{
	private final Player _player;
	private final Henna _henna;

	public HennaItemInfoPacket(Henna henna, Player player)
	{
		_henna = henna;
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_henna.getDyeId()); // symbol Id
		packetWriter.writeD(_henna.getDyeItemId()); // item id of dye

		packetWriter.writeD(0/*_henna.getCancelFee()*/); // total amount of Adena require to remove symbol
		packetWriter.writeD(_henna.isAllowedClass(_player) ? 1 : 0); // able to remove or not
		packetWriter.writeD((int) _player.getAdena());

		packetWriter.writeQ(_player.getAdena());
		packetWriter.writeD(_player.getINT()); // current INT
		packetWriter.writeH(_player.getINT() + _henna.getBaseStats(BaseStats.INT)); // equip INT
		packetWriter.writeD(_player.getSTR()); // current STR
		packetWriter.writeH(_player.getSTR() + _henna.getBaseStats(BaseStats.STR)); // equip STR
		packetWriter.writeD(_player.getCON()); // current CON
		packetWriter.writeH(_player.getCON() + _henna.getBaseStats(BaseStats.CON)); // equip CON
		packetWriter.writeD(_player.getMEN()); // current MEN
		packetWriter.writeH(_player.getMEN() + _henna.getBaseStats(BaseStats.MEN)); // equip MEN
		packetWriter.writeD(_player.getDEX()); // current DEX
		packetWriter.writeH(_player.getDEX() + _henna.getBaseStats(BaseStats.DEX)); // equip DEX
		packetWriter.writeD(_player.getWIT()); // current WIT
		packetWriter.writeH(_player.getWIT() + _henna.getBaseStats(BaseStats.WIT)); // equip WIT
		packetWriter.writeD(0x00); // current LUC
		packetWriter.writeH(0x00); // equip LUC
		packetWriter.writeD(0x00); // current CHA
		packetWriter.writeH(0x00); // equip CHA
		packetWriter.writeD(0x00); // Periodic
		return true;
	}

}
