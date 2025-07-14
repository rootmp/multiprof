package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

/**
 * Format chS c: (id) 0x39 h: (subid) 0x00 S: the character name (or maybe cmd
 * string ?)
 */
class SuperCmdCharacterInfo implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private String _characterName;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_characterName = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{}
}