package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.AllianceCrestPacket;

/**
 * @reworked by nexvill
 */
public class RequestAllyCrest implements IClientIncomingPacket
{
	// format: dddd

	private int _crestId;
	private int _clanId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readD(); // server ID
		_crestId = packet.readD();
		packet.readD(); // ally id
		_clanId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if(_crestId == 0)
			return;
		byte[] data = CrestCache.getInstance().getAllyCrest(_crestId);
		if(data != null)
		{
			AllianceCrestPacket ac = new AllianceCrestPacket(_crestId, _clanId, data);
			client.sendPacket(ac);
		}
	}
}