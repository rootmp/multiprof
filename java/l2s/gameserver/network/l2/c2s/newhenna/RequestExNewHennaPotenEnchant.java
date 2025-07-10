package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;

public class RequestExNewHennaPotenEnchant implements IClientIncomingPacket
{
	private int cSlotID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cSlotID = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		player.getHennaList().tryPotenEnchant(cSlotID);
	}
}