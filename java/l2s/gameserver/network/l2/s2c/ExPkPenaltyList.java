package l2s.gameserver.network.l2.s2c;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.templates.PlayerKiller;

public class ExPkPenaltyList implements IClientOutgoingPacket
{
	private List<PlayerKiller> _lst;
	private int _pkRefresh;

	public ExPkPenaltyList()
	{
		_pkRefresh = GameObjectsStorage.getPkRefresh();
		_lst = GameObjectsStorage.getPkPlayers();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_pkRefresh);
		packetWriter.writeD(_lst.size());
		for(PlayerKiller player : _lst)
		{
			packetWriter.writeD(player.getObjectId());
			packetWriter.writeSizedString2(player.getName(), 24);
			packetWriter.writeD(player.getLevel());
			packetWriter.writeD(player.getClassId());
		}
		return true;
	}
}
