package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestExEndScenePlayer implements IClientIncomingPacket
{
	private int _movieId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_movieId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (!activeChar.isInMovie() || activeChar.getMovieId() != _movieId)
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.endScenePlayer(false);
	}
}