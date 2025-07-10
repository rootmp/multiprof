package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExOlympiadMatchMakingResult;

public class RequestExOlympiadMatchMakingCancel extends L2GameClientPacket
{
	private int type;

	@Override
	protected boolean readImpl()
	{
		type = readC();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (!Olympiad.isRegistrationActive())
		{
			if (type == 0)
			{
				activeChar.sendPacket(SystemMsg.THE_3_VS_3_OLYMPIAD_IS_NOT_HELD_RIGHT_NOW);
			}
			else
			{
				activeChar.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
			}
			return;
		}
		Olympiad.unregisterParticipant(activeChar);
		activeChar.sendPacket(new ExOlympiadMatchMakingResult(Olympiad.isRegistered(activeChar), type));
	}
}
