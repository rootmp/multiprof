package l2s.gameserver.network.l2.s2c;

public class ExOlympiadMatchMakingResult extends L2GameServerPacket
{
	public final boolean join;
	public final int type;

	public ExOlympiadMatchMakingResult(boolean join, int type)
	{
		this.join = join;
		this.type = type;
	}

	@Override
	protected void writeImpl()
	{
		writeC(join);
		writeC(type);
	}
}
