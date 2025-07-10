package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExResetSelectMultiEnchantScroll extends L2GameServerPacket
{
	private final int _scrollObjId, _resultType;

	public ExResetSelectMultiEnchantScroll(int scrollObjId, int resultType)
	{
		_scrollObjId = scrollObjId;
		_resultType = resultType;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_scrollObjId);
		writeD(_resultType);
	}
}