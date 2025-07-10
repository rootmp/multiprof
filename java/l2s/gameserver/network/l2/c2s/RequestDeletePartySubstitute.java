package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;

public class RequestDeletePartySubstitute extends L2GameClientPacket
{
	private int _objectId;

	@Override
	protected boolean readImpl()
	{
		// _objectId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		final Party party = activeChar.getParty();
		if (party == null || party.getPartyLeader() != activeChar)
			return;

		/*
		 * TODO[Bonux]: Ertheia final Player target = World.getPlayer(_objectId);
		 * if(target != null && target.getParty() == party &&
		 * target.isPartySubstituteStarted()) { target.stopSubstituteTask();
		 * activeChar.sendPacket(new ExDeletePartySubstitute(_objectId)); }
		 */
	}
}
