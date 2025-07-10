package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ExPkPenaltyList implements IClientOutgoingPacket
{

	public ExPkPenaltyList()
	{
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
		Collection<Player> players = GameObjectsStorage.getPlayers(false, false);
		TIntSet _pks = new TIntHashSet();
		int count = 0;
		for (Player player : players)
		{
			if ((player.getKarma() < -5760) && !player.isInPeaceZone() && player.getReflection().getId() == 0)
			{
				count++;
				_pks.add(player.getObjectId());
				if (count == 30)
				{
					break;
				}
			}
		}

		packetWriter.writeD(count);
		if (count > 0)
		{
			int[] ids = _pks.toArray();
			for (int id : ids)
			{
				packetWriter.writeD(id);
				packetWriter.writeS(GameObjectsStorage.getPlayer(id).getName());
				writeString("");
				packetWriter.writeD(-2);
				packetWriter.writeD(-1);
				packetWriter.writeD(-2);
				packetWriter.writeD(-1);
				packetWriter.writeD(0); // unk
				packetWriter.writeD(0);
				packetWriter.writeD(0);
				packetWriter.writeD(0);
				packetWriter.writeD(0);
				packetWriter.writeD(GameObjectsStorage.getPlayer(id).getLevel());
				packetWriter.writeD(GameObjectsStorage.getPlayer(id).getClassId().getId());
			}
		}
	}
}
