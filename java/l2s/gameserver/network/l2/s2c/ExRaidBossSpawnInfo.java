package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import org.napile.primitive.sets.IntSet;

import l2s.gameserver.instancemanager.RaidBossSpawnManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.instances.NpcInstance;

/**
 * @author Bonux
 **/
public class ExRaidBossSpawnInfo implements IClientOutgoingPacket
{
	private final IntSet _aliveBosses;
	private int _count;
	private int[] _ids;

	public ExRaidBossSpawnInfo(int count, IntSet ids)
	{
		_aliveBosses = RaidBossSpawnManager.getInstance().getAliveRaidBosses();
		_count = count;
		_ids = ids.toArray();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_count);
		boolean baiumSpawned = false;
		for (NpcInstance npc : GameObjectsStorage.getNpcs())
		{
			if (npc.getNpcId() == 29025)
				baiumSpawned = true;
		}
		for (int i = 0; i < _count; i++)
		{
			if (_aliveBosses.contains(_ids[i]))
			{
				packetWriter.writeD(_ids[i]); // boss id
				if (GameObjectsStorage.getNpcs(true, _ids[i]).get(0).isInCombat())
				{
					packetWriter.writeD(2); // in combat
				}
				else
				{
					packetWriter.writeD(1); // alive
				}
				packetWriter.writeD(0); // time after death
			}
			else if ((_ids[i] == 29020) && baiumSpawned)
			{
				packetWriter.writeD(_ids[i]);
				packetWriter.writeD(1);
				packetWriter.writeD(0);
			}
			else
			{
				packetWriter.writeD(_ids[i]);
				packetWriter.writeD(0);
				packetWriter.writeD(0);
			}
		}
	}
}