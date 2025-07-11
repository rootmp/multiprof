package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.games.MiniGameScoreManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

/**
 * @author VISTALL
 * @date 19:55:45/25.05.2010
 */
public class RequestBR_MiniGameInsertScore implements IClientIncomingPacket
{
	private int _score;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_score = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player player = client.getActiveChar();
		if (player == null || !Config.EX_JAPAN_MINIGAME)
			return;

		MiniGameScoreManager.getInstance().insertScore(player, _score);
	}
}