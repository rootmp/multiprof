package l2s.gameserver.network.l2.s2c;

/**
 * @author nexvill
 */
public class ExInitGlobalEventUI extends L2GameServerPacket
{
	public ExInitGlobalEventUI()
	{
		//
	}

	@Override
	protected final void writeImpl()
	{
		writeD(0); // eventList size
	}
}