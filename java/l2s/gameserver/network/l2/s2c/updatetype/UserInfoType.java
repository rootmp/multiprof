package l2s.gameserver.network.l2.s2c.updatetype;

/**
 * @author Sdw
 */
public enum UserInfoType implements IUpdateTypeComponent
{
	RELATION(0x00, 4),
	BASIC_INFO(0x01, 23),
	BASE_STATS(0x02, 18),
	MAX_HPCPMP(0x03, 14),
	CURRENT_HPMPCP_EXP_SP(0x04, 39),
	ENCHANTLEVEL(0x05, 7),
	APPAREANCE(0x06, 19),
	STATUS(0x07, 6),

	STATS(0x08, 68),
	ELEMENTALS(0x09, 14),
	POSITION(0x0A, 18),
	SPEED(0x0B, 18),
	MULTIPLIER(0x0C, 18),
	COL_RADIUS_HEIGHT(0x0D, 18),
	ATK_ELEMENTAL(0x0E, 5),
	CLAN(0x0F, 32),

	SOCIAL(0x10, 34),
	VITA_FAME(0x11, 19),
	SLOTS(0x12, 12),
	MOVEMENTS(0x13, 4),
	COLOR(0x14, 10),
	INVENTORY_LIMIT(0x15, 13),
	TRUE_HERO(0x16, 9),
	ELEMENTAL(0x17, 34),

	RANKING(0x18, 6),
	STATS_INCREASE(0x19, 16),
	ADDITIONAL_STATS(0x1A, 18),
	ELIXIR_USED(0x1B, 6),
	VANGUARD_MOUNT(0x1C, 1);

	/** Int mask. */
	private final int _mask;
	private final int _blockLength;

	private UserInfoType(int mask, int blockLength)
	{
		_mask = mask;
		_blockLength = blockLength;
	}
	
	/**
	 * Gets the int mask.
	 * @return the int mask
	 */
	@Override
	public final int getMask()
	{
		return _mask;
	}

	public int getBlockLength()
	{
		return _blockLength;
	}
}