package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.PledgeCrestPacket;

/**
 * @reworked by nexvill
 */
public class RequestPledgeCrest implements IClientIncomingPacket
{
	// format: dd

	private int _pledgeId;
	private int _crestId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_pledgeId = packet.readD();
		_crestId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
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