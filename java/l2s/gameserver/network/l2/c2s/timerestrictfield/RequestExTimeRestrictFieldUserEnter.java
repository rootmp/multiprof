package l2s.gameserver.network.l2.c2s.timerestrictfield;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.timerestrictfield.ExTimeRestrictFieldUserEnter;

/**
 * @author nexvill
 */
public class RequestExTimeRestrictFieldUserEnter implements IClientIncomingPacket
{
	private int _zoneId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_zoneId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
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