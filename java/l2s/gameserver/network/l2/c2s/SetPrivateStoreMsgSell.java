package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class SetPrivateStoreMsgSell implements IClientIncomingPacket
{
	private String _storename;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_storename = packet.readS(32);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		activeChar.setSellStoreName(_storename);
		activeChar.storePrivateStore();
		activeChar.broadcastPrivateStoreInfo();
	}
}