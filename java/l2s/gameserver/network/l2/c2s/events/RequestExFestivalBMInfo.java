package l2s.gameserver.network.l2.c2s.events;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.events.ExFestivalBMAllItemInfo;
import l2s.gameserver.network.l2.s2c.events.ExFestivalBMInfo;

/**
 * @author nexvill
 */
public class RequestExFestivalBMInfo implements IClientIncomingPacket
{
	private int _openWindow;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_openWindow = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(_openWindow == 1)
		{
			activeChar.sendPacket(new ExFestivalBMInfo(activeChar));
			activeChar.sendPacket(new ExFestivalBMAllItemInfo());
		}
	}
}