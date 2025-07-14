package l2s.gameserver.network.l2.s2c;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;

public class ExPkPenaltyListOnlyLoc implements IClientOutgoingPacket
{

	public ExPkPenaltyListOnlyLoc()
	{}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
		Collection<Player> players = GameObjectsStorage.getPlayers(false, false);
		TIntSet _pks = new TIntHashSet();
		int count = 0;
		for(Player player : players)
		{
			if((player.getKarma() < -5760) && !player.isInPeaceZone() && (player.getReflection().getId() == 0))
			{
				count++;
				_pks.add(player.getObjectId());
				if(count == 30)
				{
					break;
				}
			}
		}

		packetWriter.writeD(count);
		if(count > 0)
		{
			int[] ids = _pks.toArray();
			for(int id : ids)
			{
				packetWriter.writeD(id);
				packetWriter.writeD(GameObjectsStorage.getPlayer(id).getX());
				packetWriter.writeD(GameObjectsStorage.getPlayer(id).getY());
				packetWriter.writeD(GameObjectsStorage.getPlayer(id).getZ());
			}
		}
		return true;
	}
}
