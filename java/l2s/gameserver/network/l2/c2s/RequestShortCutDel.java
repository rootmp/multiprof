package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestShortCutDel implements IClientIncomingPacket
{
	private int _slot;
	private int _page;

	/**
	 * packet type id 0x3F format: cd
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		int id = packet.readD();
		_slot = id % 12;
		_page = id / 12;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		// client dont needs confirmation. this packet is just to inform the server
		activeChar.deleteShortCut(_slot, _page);
	}
}