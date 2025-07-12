package l2s.gameserver.network.l2.s2c.updatetype;

public enum NpcInfoType implements IUpdateTypeComponent
{
	// 0
	ID(0x00, 4),
	ATTACKABLE(0x01, 1),
	RELATIONS(0x02, 8),
	NAME(0x03, 2),
	POSITION(0x04, (3 * 4)),
	HEADING(0x05, 4),
	VEHICLE(0x06, 4),
	ATK_CAST_SPEED(0x07, (2 * 4)),

	// 1
	SPEED_MODIFIER(0x08, (2 * 4)),
	EQUIPSLOT(0x09, (3 * 4)),
	STOP_MODE(0x0A, 1),//SIT = 0x0, STAND = 0x1, STORE = 0x2
	MOVE_MODE(0x0B, 1),
	COMBAT_MODE(0x0C, 0),
	IS_DEAD(0x0D, 0),
	ENVIRONMENT(0x0E, 1),// GROUND = 0x0 UNDERWATER = 0x1 AIR = 0x2 HOVER = 0x3
	EVENT_MATCH_TEAM_ID(0x0F, 1),

	// 2
	ENCHANT(0x10, 4),
	CREATURE_MOVE_TYPE(0x11, 4),
	DOPPELGANGER_SUMMONERS_ID(0x12, 4),
	EVOLUTION_ID(0x13, 4),
	IS_TARGETABLE(0x14, 0),
	IS_SHOW_NAME_TAG(0x15, 0),
	STATE(0x16, 4),
	MORPH_ID(0x17, 4),

	// 3
	CURRENT_HP(0x18, 8),
	CURRENT_MP(0x19, 4),
	MAX_HP(0x1A, 8),
	MAX_MP(0x1B, 4),
	DOPPELGANGER_TYPE(0x1C, 1),
	FOLLOWING_INFO(0x1D, (2 * 4)),
	TITLE(0x1E, 2),
	NAME_NPCSTRINGID(0x1F, 4),

	// 4
	TITLE_NPCSTRINGID(0x20, 4),
	PVP_FLAG(0x21, 1),
	REPUTATION(0x22, 4),
	DISPLAY_INFO(0x23, (5 * 4)),
	ABNORMAL_VISUAL_EFFECT(0x24, 0),
	WORLD_ID(0x25, 4),
	MASTER_ID(0x26, 4);

	public static final NpcInfoType[] VALUES = values();

	private final int _mask;
	private final int _blockLength;

	private NpcInfoType(int mask, int blockLength)
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