package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


public class RequestChangeBookMarkSlot implements IClientIncomingPacket
{
	private int slot_old, slot_new;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		slot_old = packet.readD();
		slot_new = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		// TODO not implemented
	}
}