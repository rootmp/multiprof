package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExCollectionCloseUI extends L2GameServerPacket
{
	public ExCollectionCloseUI()
	{
	}

	@Override
	protected final void writeImpl()
	{
		writeC(-56); // unknown one byte
	}
}