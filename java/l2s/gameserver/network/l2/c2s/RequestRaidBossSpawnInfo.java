package l2s.gameserver.network.l2.c2s;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExRaidBossSpawnInfo;

/**
 * @author Bonux
 **/
public class RequestRaidBossSpawnInfo implements IClientIncomingPacket
{
	private int _count;
	private IntSet _ids = new HashIntSet();

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_count = packet.readD();
		for(int i = 0; i < _count; i++)
		{
			_ids.add(packet.readD());
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		client.sendPacket(new ExRaidBossSpawnInfo(_count, _ids));
	}
}