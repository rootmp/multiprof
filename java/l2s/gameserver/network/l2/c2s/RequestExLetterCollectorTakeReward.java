package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.listener.actor.OnActorAct;
import l2s.gameserver.model.Player;

public class RequestExLetterCollectorTakeReward extends L2GameClientPacket
{
	private int letterSetId;

	@Override
	protected boolean readImpl()
	{
		letterSetId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		player.getListeners().onAct(OnActorAct.EX_LETTER_COLLECTOR_TAKE_REWARD, letterSetId);
	}
}
