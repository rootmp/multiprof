package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.PledgeCrestPacket;

/**
 * @reworked by nexvill
 */
public class RequestPledgeCrest extends L2GameClientPacket
{
	// format: dd

	private int _pledgeId;
	private int _crestId;

	@Override
	protected boolean readImpl()
	{
		_pledgeId = readD();
		_crestId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		if (_crestId == 0)
			return;
		byte[] data = CrestCache.getInstance().getPledgeCrest(_crestId);
		if (data != null)
		{
			PledgeCrestPacket pc = new PledgeCrestPacket(_pledgeId, _crestId, data);
			sendPacket(pc);
		}
	}
}