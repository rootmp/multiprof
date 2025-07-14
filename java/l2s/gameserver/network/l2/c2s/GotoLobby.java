package l2s.gameserver.network.l2.c2s;

import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.CharacterSelectionInfoPacket;

public class GotoLobby implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		List<Integer> players = CharacterDAO.getInstance().getPlayersIdByAccount(client.getLogin());
		for(Integer oid : players)
		{
			Player player = GameObjectsStorage.getPlayer(oid);
			if(player != null)
				player.kick();
		}
		client.sendPacket(new CharacterSelectionInfoPacket(client));
	}
}