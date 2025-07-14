package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;

public final class RequestRegistWaitingSubstitute implements IClientIncomingPacket
{
	private boolean _enable;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_enable = packet.readD() == 1;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(activeChar.isAutoSearchParty() != _enable)
		{
			if(_enable)
			{
				if(activeChar.mayPartySearch(true, true))
					activeChar.enableAutoSearchParty();
			}
			else
				activeChar.disablePartySearch(true);

			if(_enable)
				activeChar.sendPacket(SystemMsg.YOU_ARE_REGISTERED_ON_THE_WAITING_LIST);
			else
				activeChar.sendPacket(SystemMsg.STOPPED_SEARCHING_THE_PARTY);
		}
	}
}