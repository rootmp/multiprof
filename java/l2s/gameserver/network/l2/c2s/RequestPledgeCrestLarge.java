package l2s.gameserver.network.l2.c2s;
import java.util.Arrays;

import gnu.trove.map.TIntObjectMap;
import l2s.commons.network.PacketReader;
import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExPledgeEmblem;

/**
 * @author Bonux
 **/
public class RequestPledgeCrestLarge implements IClientIncomingPacket
{
	// format: chdd
	private int _crestId;
	private int _pledgeId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_crestId = packet.readD();
		_pledgeId = packet.readD();
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

		if (_pledgeId == 0)
			return;

		TIntObjectMap<byte[]> data = CrestCache.getInstance().getPledgeCrestLarge(_crestId);
		if (data != null)
		{
			int totalSize = CrestCache.getByteMapSize(data);
			int[] keys = data.keys();
			Arrays.sort(keys);
			for (int key : keys)
				client.sendPacket(new ExPledgeEmblem(_pledgeId, _crestId, key, totalSize, data.get(key)));
		}
	}
}