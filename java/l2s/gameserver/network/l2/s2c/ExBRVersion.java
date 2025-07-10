package l2s.gameserver.network.l2.s2c;

/**
 * @author nexvill
 */
public class ExBRVersion extends L2GameServerPacket
{
	public ExBRVersion()
	{
		//
	}

	@Override
	protected final void writeImpl()
	{
		writeC(1);
	}
}