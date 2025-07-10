package l2s.gameserver.network.l2.s2c;

/**
 * @reworked by nexvill
 */
public class AllianceCrestPacket extends L2GameServerPacket
{
	private int _crestId;
	private int _clanId;
	private byte[] _data;

	public AllianceCrestPacket(int crestId, int clanId, byte[] data)
	{
		_crestId = crestId;
		_clanId = clanId;
		_data = data;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_clanId);
		writeD(_crestId);
		if (_data.length > 0)
		{
			writeD(_data.length);
			writeD(_data.length);
			writeB(_data);
		}
		else
		{
			writeD(0);
		}
	}
}