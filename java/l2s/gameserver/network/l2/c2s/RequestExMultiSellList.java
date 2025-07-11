package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.MultiSellHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestExMultiSellList implements IClientIncomingPacket
{
	private int _listId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_listId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		MultiSellHolder.getInstance().SeparateAndSend(_listId, activeChar, 0);
	}
}