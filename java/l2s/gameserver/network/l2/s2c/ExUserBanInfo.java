package l2s.gameserver.network.l2.s2c;

/**
 * @author Bonux
 **/
public class ExUserBanInfo extends L2GameServerPacket
{
	private final int _points;

	public ExUserBanInfo(int points)
	{
		_points = points;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_points);
	}
}