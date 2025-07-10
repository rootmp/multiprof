package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Request;
import l2s.gameserver.model.Request.L2RequestType;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ActionFailPacket;
import l2s.gameserver.network.l2.s2c.JoinPartyPacket;

public class RequestAnswerJoinParty extends L2GameClientPacket
{
	private int _response;

	@Override
	protected boolean readImpl()
	{
		if (_buf.hasRemaining())
			_response = readD();
		else
			_response = 0;
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		Request request = activeChar.getRequest();
		if (request == null || !request.isTypeOf(L2RequestType.PARTY))
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
			activeChar.sendPacket(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE);
			activeChar.sendActionFailed();
			return;
		}

		if (requestor.getRequest() != request)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}

		// отказ(0) или автоматический отказ(-1)
		if (_response <= 0)
		{
			request.cancel();
			requestor.sendPacket(JoinPartyPacket.FAIL);
			return;
		}

		if (activeChar.isInOlympiadMode())
		{
			request.cancel();
			activeChar.sendPacket(SystemMsg.A_PARTY_CANNOT_BE_FORMED_IN_THIS_AREA);
			requestor.sendPacket(JoinPartyPacket.FAIL);
			return;
		}

		if (requestor.isInOlympiadMode())
		{
			request.cancel();
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_INVITE_A_FRIEND_OR_PARTY_WHILE_PARTICIPATING_IN_THE_CEREMONY_OF_CHAOS);
			requestor.sendPacket(JoinPartyPacket.FAIL);
			return;
		}

		Party party = requestor.getParty();

		if (party != null && party.getMemberCount() >= Party.MAX_SIZE)
		{
			request.cancel();
			activeChar.sendPacket(SystemMsg.THE_PARTY_IS_FULL);
			requestor.sendPacket(SystemMsg.THE_PARTY_IS_FULL);
			requestor.sendPacket(JoinPartyPacket.FAIL);
			return;
		}

		IBroadcastPacket problem = activeChar.canJoinParty(requestor);
		if (problem != null)
		{
			request.cancel();
			activeChar.sendPacket(problem, ActionFailPacket.STATIC);
			requestor.sendPacket(JoinPartyPacket.FAIL);
			return;
		}

		if (party == null)
		{
			int itemDistribution = request.getInteger("itemDistribution");
			requestor.setParty(party = new Party(requestor, itemDistribution));
		}

		try
		{
			activeChar.joinParty(party, false);
			requestor.sendPacket(JoinPartyPacket.SUCCESS);
		}
		finally
		{
			request.done();
		}
	}
}