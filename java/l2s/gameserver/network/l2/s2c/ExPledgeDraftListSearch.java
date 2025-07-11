package l2s.gameserver.network.l2.s2c;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.clansearch.ClanSearchManager;
import l2s.gameserver.model.clansearch.ClanSearchPlayer;
import l2s.gameserver.model.clansearch.ClanSearchWaiterParams;

/**
 * @author GodWorld
 * @reworked by Bonux
 **/
public class ExPledgeDraftListSearch implements IClientOutgoingPacket
{
	private final List<ClanSearchPlayer> _waiters;

	public ExPledgeDraftListSearch(ClanSearchWaiterParams params)
	{
		_waiters = ClanSearchManager.getInstance().listWaiters(params);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_waiters.size());
		for (ClanSearchPlayer waiter : _waiters)
		{
			packetWriter.writeD(waiter.getCharId());
			packetWriter.writeS(waiter.getName());
			packetWriter.writeD(waiter.getSearchType().ordinal());
			packetWriter.writeD(waiter.getClassId());
			packetWriter.writeD(waiter.getLevel());
		}
		return true;
	}
}