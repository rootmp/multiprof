package l2s.gameserver.network.l2.c2s.timerestrictfield;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.timerestrictfield.ExTimeRestrictFieldUserEnter;

/**
 * @author nexvill
 */
public class RequestExTimeRestrictFieldUserEnter extends L2GameClientPacket
{
	private int _zoneId;

	@Override
	protected boolean readImpl()
	{
		_zoneId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isInCombat())
		{
			activeChar.sendPacket(SystemMsg.NOT_AVAILABLE_IN_COMBAT);
			return;
		}

		if (_zoneId > 100)
		{
			int izId = 0;
			if (_zoneId < 105)
			{
				izId = _zoneId + 107;
			}
			else
			{
				izId = _zoneId + 106;
			}

			if (activeChar.getActiveReflection() != null)
			{
				if (activeChar.getActiveReflection().getId() != izId)
				{
					activeChar.sendPacket(SystemMsg.YOU_HAVE_ENTERED_ANOTHER_INSTANCE_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON);
					return;
				}
			}
		}

		if (activeChar.isInOlympiadMode() || Olympiad.isRegistered(activeChar))
		{
			activeChar.sendPacket(SystemMsg.SPECIAL_INSTANCE_ZONES_CANNOT_BE_USED_WHILE_WAITING_FOR_THE_OLYMPIAD);
			return;
		}

		activeChar.sendPacket(new ExTimeRestrictFieldUserEnter(activeChar, _zoneId));
	}
}