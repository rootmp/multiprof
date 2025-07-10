package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExOlympiadMatchMakingResult;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;

public class RequestExOlympiadMatchMaking extends L2GameClientPacket
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

		if (activeChar.isInventoryFull())
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_AS_THE_INVENTORY_WEIGHT__SLOT_IS_FILLED_BEYOND_80).addName(activeChar));
			return;
		}

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
		Olympiad.registerParticipant(activeChar);
		activeChar.sendPacket(new ExOlympiadMatchMakingResult(Olympiad.isRegistered(activeChar), type));
	}
}
