package l2s.gameserver.handler.usercommands.impl;

import l2s.gameserver.handler.usercommands.IUserCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExOlympiadRecord;

/**
 * Support for /olympiadstat command
 */
public class OlympiadStat implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		109
	};

	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		if (id != COMMAND_IDS[0])
			return false;

		if (activeChar.isInOlympiadMode() || activeChar.isInObserverMode())
		{
			activeChar.sendPacket(SystemMsg.UNABLE_TO_OPEN_OLYMPIAD_SCREEN_WHILE_IN_PARTICIPATING_OR_WATCHING_A_MATCH);
			return true;
		}

		activeChar.sendPacket(new ExOlympiadRecord(activeChar));
		return true;
	}

	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}