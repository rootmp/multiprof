package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

/**
 * Format chS c: (id) 0x39 h: (subid) 0x01 S: the summon name (or maybe cmd
 * string ?)
 */
class SuperCmdSummonCmd implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private String _summonName;

	/**
	 * @param buf
	 * @param client
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_summonName = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{}
}