package l2s.gameserver.network.l2.c2s.teleport;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.teleport.ExSharedPositionTeleportUI;

/**
 * @author nexvill
 */
public class RequestExSharedPositionTeleportUI implements IClientIncomingPacket
{
	private int _allow, _tpId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_allow = packet.readC();
		_tpId = packet.readH(); // tp id
		packet.readC(); // ??
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();

		if(_allow == 1)
			player.sendPacket(new ExSharedPositionTeleportUI(player, _tpId));
	}
}