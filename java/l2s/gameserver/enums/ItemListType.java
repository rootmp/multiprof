package l2s.gameserver.enums;

import l2s.gameserver.network.l2.s2c.updatetype.IUpdateTypeComponent;

public enum ItemListType implements IUpdateTypeComponent
{
	IS_AUGMENTED(1, 12),
	IS_ELEMENTED(2, 16),
	VISUAL_CHANGED(4, 4),
	HAVE_ENSOUL(8, 14),
	USED_COUNT(16, 2),
	REUSE_DELAY(32, 4),
	EVOLVE(64, 28),
	IS_BLESSED(128, 1),
	IS_ILLUSORY(256, 1);

	private final int _mask;
	private final int _blockLength;

	private ItemListType(int mask, int blockLength)
	{
		_mask = mask;
		_blockLength = blockLength;

	}

	@Override
	public int getMask()
	{
		return _mask;
	}

	public int getBlockLength()
	{
		return _blockLength;
	}
}
