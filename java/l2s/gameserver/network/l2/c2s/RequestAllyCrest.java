package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.network.l2.s2c.AllianceCrestPacket;

/**
 * @reworked by nexvill
 */
public class RequestAllyCrest extends L2GameClientPacket
{
	// format: dddd

	private int _crestId;
	private int _clanId;

	@Override
	protected boolean readImpl()
	{
		readD(); // server ID
		_crestId = readD();
		readD(); // ally id
		_clanId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		if (_crestId == 0)
			return;
		byte[] data = CrestCache.getInstance().getAllyCrest(_crestId);
		if (data != null)
		{
			AllianceCrestPacket ac = new AllianceCrestPacket(_crestId, _clanId, data);
			sendPacket(ac);
		}
	}
}