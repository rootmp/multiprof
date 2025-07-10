package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExMissionLevelRewardList;

/**
 * @author nexvill
 */
public class RequestExMissionLevelRewardList extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}

		sendPacket(new ExMissionLevelRewardList(player));
	}
}