package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExBR_ProductInfoPacket;

public class RequestExBrProductInfoReq implements IClientIncomingPacket
{
	private int _productId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_productId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if(!Config.EX_USE_PRIME_SHOP)
			return;

		Player activeChar = client.getActiveChar();

		if(activeChar == null)
			return;

		activeChar.sendPacket(new ExBR_ProductInfoPacket(activeChar, _productId));

	}
}