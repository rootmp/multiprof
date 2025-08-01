package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.newhenna.ExNewHennaPotenOpenslot;

public class RequestExNewHennaPotenOpenslot implements IClientIncomingPacket
{
	private int nSlotID;
	private int nReqOpenSlotStep;
	private int nCostItemClassID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{

		nSlotID = packet.readD();
		nReqOpenSlotStep = packet.readD();
		nCostItemClassID = packet.readD();

		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		player.sendPacket(new ExNewHennaPotenOpenslot());
	}
}
