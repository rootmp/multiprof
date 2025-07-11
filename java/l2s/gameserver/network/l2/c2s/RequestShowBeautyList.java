package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExResponseBeautyListPacket;

/**
 * @author Bonux
**/
public class RequestShowBeautyList implements IClientIncomingPacket
{
	private int _type;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_type = packet.readD(); // 0 причёска, 1 лицо
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		activeChar.sendPacket(new ExResponseBeautyListPacket(activeChar, _type));
	}
}
