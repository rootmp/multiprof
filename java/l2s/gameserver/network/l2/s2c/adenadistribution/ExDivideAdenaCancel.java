package l2s.gameserver.network.l2.s2c.adenadistribution;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author Sdw
 */
public class ExDivideAdenaCancel extends L2GameServerPacket
{
	public static final L2GameServerPacket STATIC = new ExDivideAdenaCancel();

	@Override
	protected final void writeImpl()
	{
		writeC(0x00);
	}
}