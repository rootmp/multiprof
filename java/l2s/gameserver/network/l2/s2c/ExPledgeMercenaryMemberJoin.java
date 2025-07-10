package l2s.gameserver.network.l2.s2c;

public class ExPledgeMercenaryMemberJoin extends L2GameServerPacket
{
	private final int type;
	private final boolean join;
	private final int playerObjectId;
	private final int clanObjectId;

	public ExPledgeMercenaryMemberJoin(int type, boolean join, int playerObjectId, int clanObjectId)
	{
		this.type = type;
		this.join = join;
		this.playerObjectId = playerObjectId;
		this.clanObjectId = clanObjectId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(type); // type
		writeD(join); // entered
		writeD(playerObjectId);
		writeD(clanObjectId);
	}
}
