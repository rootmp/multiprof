package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.CharacterSelectionInfo;

public class CharacterRestore extends L2GameClientPacket
{
	// cd
	private int _charSlot;

	@Override
	protected boolean readImpl()
	{
		_charSlot = readD();
		return true;
	}

	@Override
	protected void runImpl()
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