package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient.GameClientState;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ActionFailPacket;
import l2s.gameserver.network.l2.s2c.CharacterSelectionInfo;
import l2s.gameserver.network.l2.s2c.RestartResponsePacket;

public class RequestRestart extends L2GameClientPacket
{
	/**
	 * packet type id 0x57 format: c
	 */

	@Override
	protected boolean readImpl()
	{
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();

		if (activeChar == null)
			return;

		if (activeChar.isInObserverMode())
		{
			activeChar.sendPacket(SystemMsg.OBSERVERS_CANNOT_PARTICIPATE, RestartResponsePacket.FAIL, ActionFailPacket.STATIC);
			return;
		}

		if (activeChar.isInCombat() && !activeChar.isGM())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_RESTART_WHILE_IN_COMBAT, RestartResponsePacket.FAIL, ActionFailPacket.STATIC);
			return;
		}

		if (activeChar.isFishing() && !activeChar.isGM())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING_2, RestartResponsePacket.FAIL, ActionFailPacket.STATIC);
			return;
		}

		if (activeChar.isBlocked() && !activeChar.isInTrainingCamp() && !activeChar.isFlying() && !activeChar.isInAwayingMode())
		{
			activeChar.sendMessage(new CustomMessage("l2s.gameserver.network.l2.c2s.RequestRestart.OutOfControl"));
			activeChar.sendPacket(RestartResponsePacket.FAIL, ActionFailPacket.STATIC);
			return;
		}

		activeChar.stopTimedHuntingZoneTask(true, true);

		if (activeChar.isInFightClub())
		{
			activeChar.sendMessage("You need to leave Fight Club first!");
			activeChar.sendPacket(RestartResponsePacket.FAIL, ActionFailPacket.STATIC);
			return;
		}

		if (getClient() != null)
			getClient().setState(GameClientState.AUTHED);

		activeChar.restart();
		// send char list
		CharacterSelectionInfo cl = new CharacterSelectionInfo(getClient());
		sendPacket(RestartResponsePacket.OK, cl);
		getClient().setCharSelection(cl.getCharInfo());
	}
}