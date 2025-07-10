package l2s.gameserver.network.l2.c2s.subjugation;

import java.util.Map;

import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationRanking;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class RequestExSubjugationRanking extends L2GameClientPacket
{
	private int _zoneId;

	@Override
	protected boolean readImpl()
	{
		_zoneId = readD(); // zone id
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
		if (player == null)
			return;

		Map<Integer, StatsSet> result = RankManager.getInstance().getSubjugationRanks(_zoneId);
		player.sendPacket(new ExSubjugationRanking(player, _zoneId, result));
	}
}