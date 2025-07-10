package l2s.gameserver.network.l2.s2c.events;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExBalthusEventJackpotUser extends L2GameServerPacket
{
	public ExBalthusEventJackpotUser()
	{
		//
	}

	@Override
	protected final void writeImpl()
	{
		writeC(1);
	}
}