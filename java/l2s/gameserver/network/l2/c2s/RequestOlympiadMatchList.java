package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.ObservableArena;
import l2s.gameserver.model.Player;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author Bonux
 **/
public class RequestOlympiadMatchList implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		// trigger
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		if (NpcUtils.canPassPacket(player, this) != null)
			return;

		ObservableArena arena = player.getObservableArena();
		if (arena == null)
			return;

		arena.showObservableArenasList(player);
	}
}
