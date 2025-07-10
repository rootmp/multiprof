package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExOlympiadMatchMakingResult;

public class RequestExOlympiadMatchMakingCancel implements IClientIncomingPacket
{
	private int type;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		type = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
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
