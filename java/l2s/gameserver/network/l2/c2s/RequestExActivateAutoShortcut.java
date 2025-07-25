package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestExActivateAutoShortcut implements IClientIncomingPacket
{
	private int _slot;
	private boolean _activate;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slot = packet.readH();
		_activate = packet.readC() > 0;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;

		if(player.getAutoShortCuts().activate(_slot, _activate))
			return;

		player.sendActionFailed();
	}
}
