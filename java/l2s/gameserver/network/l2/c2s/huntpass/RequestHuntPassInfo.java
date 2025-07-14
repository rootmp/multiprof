package l2s.gameserver.network.l2.c2s.huntpass;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassInfo;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassSayhasSupportInfo;

public class RequestHuntPassInfo implements IClientIncomingPacket
{
	private int _passType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_passType = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		player.sendPacket(new HuntPassInfo(player, _passType));
		player.sendPacket(new HuntPassSayhasSupportInfo(player));
	}
}
