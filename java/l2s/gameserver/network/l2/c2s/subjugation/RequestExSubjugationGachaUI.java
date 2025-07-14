package l2s.gameserver.network.l2.c2s.subjugation;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationGachaUI;

/**
 * @author nexvill
 */
public class RequestExSubjugationGachaUI implements IClientIncomingPacket
{
	private int _zoneId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_zoneId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		player.sendPacket(new ExSubjugationGachaUI(player, _zoneId));
	}
}