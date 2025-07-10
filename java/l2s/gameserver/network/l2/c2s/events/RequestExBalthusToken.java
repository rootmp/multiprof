package l2s.gameserver.network.l2.c2s.events;

import java.time.ZonedDateTime;

import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.events.ExBalthusEvent;

/**
 * @author nexvill
 */
public class RequestExBalthusToken extends L2GameClientPacket
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

		int amount = player.getVarInt(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, 0);
		if (amount == 0)
		{
			return;
		}

		player.getInventory().addItem(Config.BALTHUS_EVENT_BASIC_REWARD_ID, amount);
		player.setVar(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, 0);

		int round = ServerVariables.getInt("balthus_round");
		int stage = ServerVariables.getInt("balthus_stage");
		int jackpotId = ServerVariables.getInt("balthus_jackpot");
		boolean receivedThisHour = ServerVariables.getBool("balthus_received_hour");
		boolean participate = false;
		if (player.getAbnormalList().contains(Config.BALTHUS_EVENT_PARTICIPATE_BUFF_ID))
		{
			participate = true;
		}
		int time = ZonedDateTime.now().getMinute() * 60;

		player.sendPacket(new ExBalthusEvent(round, stage, jackpotId, Config.BALTHUS_EVENT_BASIC_REWARD_COUNT, 0, participate, receivedThisHour, time));
	}
}