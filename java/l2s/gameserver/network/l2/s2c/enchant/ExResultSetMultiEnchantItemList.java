package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExResultSetMultiEnchantItemList extends L2GameServerPacket
{
	private final int _resultType;

	public ExResultSetMultiEnchantItemList(int resultType)
	{
		_resultType = resultType;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_resultType);
	}
}