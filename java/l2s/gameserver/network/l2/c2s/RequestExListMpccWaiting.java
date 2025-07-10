package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExListMpccWaiting;

/**
 * @author VISTALL
 */
public class RequestExListMpccWaiting implements IClientIncomingPacket
{
	private int _listId;
	private int _locationId;
	private boolean _allLevels;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_listId = packet.readD();
		_locationId = packet.readD();
		_allLevels = readD() == 1;
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new ExListMpccWaiting(player, _listId, _locationId, _allLevels));
	}
}