package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
//import l2s.gameserver.model.actor.instances.player.FlyMove;
import l2s.gameserver.network.l2.GameClient;

public final class RequestFlyMove implements IClientIncomingPacket
{
	private int _pointId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_pointId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

	/*	final FlyMove flyMove = activeChar.getFlyMove();
		if(flyMove == null)
			return;

		flyMove.move(_pointId);*/
	}
}
