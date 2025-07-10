package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.Request;
import l2s.gameserver.model.Request.L2RequestType;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.ClanWar;
import l2s.gameserver.model.pledge.ClanWar.ClanWarPeriod;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;

/**
 * @author GodWorld & reworked by Bonux
 **/
public final class RequestReplyStartPledgeWar implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestReplyStartPledgeWar.class);

	private int _answer;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		/* String _reqName = */packet.readS();
		_answer = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Request request = activeChar.getRequest();
		if (request == null || !request.isTypeOf(L2RequestType.CLAN_WAR_START))
			return;

		if (!request.isInProgress())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isOutOfControl())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}

		Player requestor = request.getRequestor();
		if (requestor == null)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}

		if (requestor.getRequest() != request)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}

		Clan clan = requestor.getClan();
		if (clan == null)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}

		if (_answer == 1)
		{
			try
			{
				ClanWar war = clan.getWarWith(activeChar.getClanId());
				if (war == null)
				{
					_log.warn(getClass().getSimpleName() + ": Opponent clan war object not found!");

					request.cancel();
					activeChar.sendActionFailed();
					return;
				}

				if (war.getPeriod() != ClanWarPeriod.PREPARATION)
				{
					request.cancel();
					activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_HAVE_ALREADY_BEEN_AT_WAR_WITH_THE_S1_CLAN_5_DAYS_MUST_PASS_BEFORE_YOU_CAN_DECLARE_WAR_AGAIN).addString(requestor.getClan().getName()));
					return;
				}

				war.setPeriod(ClanWarPeriod.MUTUAL);
			}
			finally
			{
				request.done();
			}
		}
		else
		{
			requestor.sendPacket(SystemMsg.THE_S1_CLAN_DID_NOT_RESPOND_WAR_PROCLAMATION_HAS_BEEN_REFUSED);
			request.cancel();
		}
	}
}