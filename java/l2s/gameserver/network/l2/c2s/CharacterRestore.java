package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.CharacterSelectionInfo;

public class CharacterRestore implements IClientIncomingPacket
{
	// cd
	private int _charSlot;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_charSlot = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		GameClient client = getClient();
		try
		{
			client.markRestoredChar(_charSlot);
		}
		catch (Exception e)
		{
		}
		CharacterSelectionInfo cl = new CharacterSelectionInfo(client);
		sendPacket(cl);
		client.setCharSelection(cl.getCharInfo());
	}
}