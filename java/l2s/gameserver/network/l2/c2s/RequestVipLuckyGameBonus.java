package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

/**
 * @author Bonux
 **/
public final class RequestVipLuckyGameBonus implements IClientIncomingPacket
{
	private int _unk1;
	private int _unk2;

	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unk1 = packet.readC();
		_unk2 = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		System.out.println("RequestVipLuckyGameBonus _unk1=" + _unk1 + ", _unk2=" + _unk2);
	}
}