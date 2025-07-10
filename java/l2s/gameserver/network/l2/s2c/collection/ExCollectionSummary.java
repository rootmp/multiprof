package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExCollectionSummary extends L2GameServerPacket
{
	public ExCollectionSummary()
	{
	}

	@Override
	protected final void writeImpl()
	{
		writeD(0);
		// int count = 0;
		// writeD(count); // count
		// for (int i = 0; i < count; i++)
		// {
		// writeH(0); // collection id
		// writeD(0); // expiration time in seconds
		// }
	}
}