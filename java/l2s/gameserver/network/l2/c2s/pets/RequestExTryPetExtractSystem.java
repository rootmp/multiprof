package l2s.gameserver.network.l2.c2s.pets;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;

/**
 * @author nexvill
 */
public class RequestExTryPetExtractSystem implements IClientIncomingPacket
{
	private int _petItemId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_petItemId = packet.readD();
		System.out.print("received RequestExTryPetExtractSystem with data: " + _petItemId);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
	}
}