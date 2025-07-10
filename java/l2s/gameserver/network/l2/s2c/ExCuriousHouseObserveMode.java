package l2s.gameserver.network.l2.s2c;

/**
 * @author Bonux
 **/
public class ExCuriousHouseObserveMode extends L2GameServerPacket
{
	public static final L2GameServerPacket ENTER = new ExCuriousHouseObserveMode(false);
	public static final L2GameServerPacket LEAVE = new ExCuriousHouseObserveMode(true);

	private final boolean _leave;

	public ExCuriousHouseObserveMode(boolean leave)
	{
		_leave = leave;
	}

	@Override
	protected void writeImpl()
	{
		writeC(_leave);
	}
}
