package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestDeletePartySubstitute implements IClientIncomingPacket
{
	private int _objectId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		// _objectId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		final Party party = activeChar.getParty();
		if(party == null || party.getPartyLeader() != activeChar)
			return;

		/*
		 * TODO[Bonux]: Ertheia final Player target = World.getPlayer(_objectId);
		 * if(target != null && target.getParty() == party &&
		 * target.isPartySubstituteStarted()) { target.stopSubstituteTask();
		 * activeChar.sendPacket(new ExDeletePartySubstitute(_objectId)); }
		 */
	}
}
