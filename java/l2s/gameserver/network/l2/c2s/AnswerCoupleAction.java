package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.ai.PlayableAI;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Request;
import l2s.gameserver.model.Request.L2RequestType;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.utils.PositionUtils;

public class AnswerCoupleAction implements IClientIncomingPacket
{
	private int _charObjId;
	private int _actionId;
	private int _answer;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_actionId = packet.readD();
		_answer = packet.readD();
		_charObjId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Request request = activeChar.getRequest();
		if (request == null || !request.isTypeOf(L2RequestType.COUPLE_ACTION))
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

		if (requestor.getObjectId() != _charObjId || requestor.getRequest() != request)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}

		switch (_answer)
		{
			case -1: // refused
				requestor.sendPacket(new SystemMessagePacket(SystemMsg.C1_IS_SET_TO_REFUSE_COUPLE_ACTIONS_AND_CANNOT_BE_REQUESTED_FOR_A_COUPLE_ACTION).addName(activeChar));
				request.cancel();
				break;
			case 0: // cancel
				activeChar.sendPacket(SystemMsg.THE_COUPLE_ACTION_WAS_DENIED);
				requestor.sendPacket(SystemMsg.THE_COUPLE_ACTION_WAS_CANCELLED);
				requestor.sendActionFailed();
				request.cancel();
				break;
			case 1: // ok
				try
				{
					if (!checkCondition(activeChar, requestor) || !checkCondition(requestor, activeChar))
						return;

					Location loc = PositionUtils.applyOffset(activeChar, activeChar.getLoc(), 25);

					loc = GeoEngine.moveCheck(requestor.getX(), requestor.getY(), requestor.getZ(), loc.x, loc.y, requestor.getGeoIndex());
					if (loc != null)
					{
						requestor.getMovement().moveToLocation(loc, 0, false);
						requestor.getAI().setNextAction(PlayableAI.AINextAction.COUPLE_ACTION, activeChar, _actionId, true, false);
					}
				}
				finally
				{
					request.done();
				}
				break;
		}
	}

	private static boolean checkCondition(Player activeChar, Player requestor)
	{
		if (!activeChar.isInRange(requestor, 300) || activeChar.isInRange(requestor, 25) || !GeoEngine.canSeeTarget(activeChar, requestor))
		{
			activeChar.sendPacket(SystemMsg.THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS);
			return false;
		}
		return activeChar.checkCoupleAction(requestor);
	}
}
