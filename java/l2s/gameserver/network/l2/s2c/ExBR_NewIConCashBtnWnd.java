package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class ExBR_NewIConCashBtnWnd implements IClientOutgoingPacket
{
	private final int _value;

	public ExBR_NewIConCashBtnWnd(Player player)
	{
		_value = player.getProductHistoryList().haveGifts() ? 0x02 : 0x00;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_value); // Has Updates
		return true;
	}
}
