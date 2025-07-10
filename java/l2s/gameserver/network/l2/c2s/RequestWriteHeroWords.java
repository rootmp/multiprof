package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.Hero;

/**
 * Format chS c (id) 0xD0 h (subid) 0x0C S the hero's words :)
 */
public class RequestWriteHeroWords implements IClientIncomingPacket
{
	private String _heroWords;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_heroWords = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null || !player.isHero())
			return;

		if (_heroWords == null || _heroWords.length() > 300)
			return;

		Hero.getInstance().setHeroMessage(player.getObjectId(), _heroWords);
	}
}