package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


/**
 * format: ddd
 */
public class NetPing implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int unk, unk2, unk3;

	@Override
	public void run(GameClient client)
	{
		// _log.info.println(getType() + " :: " + unk + " :: " + unk2 + " :: " + unk3);
	}

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		unk = packet.readD();
		unk2 = packet.readD();
		unk3 = packet.readD();
		return true;
	}
}