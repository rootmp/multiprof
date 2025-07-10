package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;

/**
 * Format: (c) S S: pledge name?
 */
public class RequestPledgeExtendedInfo implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private String _name;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_name = readS(16);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		if (activeChar.isGM())
			activeChar.sendMessage("RequestPledgeExtendedInfo");

		// TODO this
	}
}