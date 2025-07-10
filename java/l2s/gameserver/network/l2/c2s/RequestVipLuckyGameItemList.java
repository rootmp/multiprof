package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


/**
 * @author Bonux
 **/
public final class RequestVipLuckyGameItemList implements IClientIncomingPacket
{
	private int _unk1;

	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unk1 = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		System.out.println("RequestVipLuckyGameItemList _unk1=" + _unk1);
	}
}