package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Request;
import l2s.gameserver.model.Request.L2RequestType;
import l2s.gameserver.model.matching.MatchingRoom;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;

/**
 * format: (ch)d
 */
public class AnswerJoinPartyRoom implements IClientIncomingPacket
{
	private int _response;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		if (packet.hasRemaining())
			_response = packet.readD();
		else
			_response = 0;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Request request = activeChar.getRequest();
		if (request == null || !request.isTypeOf(L2RequestType.PARTY_ROOM))
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

		// отказ
		if (_response == 0)
		{
			request.cancel();
			requestor.sendPacket(SystemMsg.THE_PLAYER_DECLINED_TO_JOIN_YOUR_PARTY);
			return;
		}

		if (activeChar.getMatchingRoom() != null)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}

		try
		{
			MatchingRoom room = requestor.getMatchingRoom();
			if (room == null || room.getType() != MatchingRoom.PARTY_MATCHING)
				return;

			room.addMember(activeChar);
		}
		finally
		{
			request.done();
		}
	}
}