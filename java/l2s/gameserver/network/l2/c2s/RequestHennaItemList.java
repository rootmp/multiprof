package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.HennaEquipListPacket;

/**
 * @author Tempy, Zoey76
 */
public class RequestHennaItemList implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int _unknown;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unknown = packet.readD(); // TODO: Identify.
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player != null)
		{
			player.sendPacket(new HennaEquipListPacket(player));
		}
	}
}
