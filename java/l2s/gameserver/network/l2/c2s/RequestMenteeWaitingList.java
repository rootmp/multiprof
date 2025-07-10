package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;

public class RequestMenteeWaitingList implements IClientIncomingPacket
{
	private int maxLevel;
	private int minLevel;
	private int page;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		this.page = packet.readD();
		this.minLevel = packet.readD();
		this.maxLevel = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if ((player == null) || maxLevel == 0 || minLevel == 0 || page == 0)
			return;
		player.sendMenteeList();
		player.sendUserInfo(true);
	}
}