package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.HennaEquipListPacket;

public class RequestHennaList implements IClientIncomingPacket
{
	private int unk;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		unk = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new HennaEquipListPacket(player));
	}
}