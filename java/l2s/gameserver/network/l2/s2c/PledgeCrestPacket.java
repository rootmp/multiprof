package l2s.gameserver.network.l2.s2c;

/**
 * @reworked by nexvill
 */
public class PledgeCrestPacket extends L2GameServerPacket
{
	private int _clanId;
	private int _crestId;
	private int _crestSize;
	private byte[] _data;

	public PledgeCrestPacket(int clanId, int crestId, byte[] data)
	{
		_clanId = clanId;
		_crestId = crestId;
		_data = data;
		_crestSize = _data.length;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_clanId);
		writeD(_crestId);
		if (_crestSize != 0)
		{
			writeD(_crestSize);
			writeD(_crestSize);
			writeB(_data);
		}
		else
		{
			writeD(0);
		}
	}
}