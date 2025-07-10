package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;

public class SnoopQuit implements IClientIncomingPacket
{
	private int _snoopID;

	/**
	 * format: cd
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_snoopID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Player player = (Player) GameObjectsStorage.findObject(_snoopID);
		if (player == null)
			return;

		player.removeSnooper(activeChar);
	}
}