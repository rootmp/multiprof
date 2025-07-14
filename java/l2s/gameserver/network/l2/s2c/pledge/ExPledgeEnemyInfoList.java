package l2s.gameserver.network.l2.s2c.pledge;

import java.util.List;
import java.util.stream.Collectors;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.ClanWar;
import l2s.gameserver.model.pledge.ClanWar.ClanWarState;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExPledgeEnemyInfoList implements IClientOutgoingPacket
{
	private final Clan playerClan;
	private List<ClanWar> _warList;

	public ExPledgeEnemyInfoList(Clan playerClan)
	{
		this.playerClan = playerClan;
		_warList = playerClan.getWars().valueCollection().stream().filter(it -> (it.getClanWarState(playerClan) == ClanWarState.PREPARATION)
				|| (it.getClanWarState(playerClan) == ClanWarState.MUTUAL)).collect(Collectors.toList());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_warList.size());

		for(ClanWar war : _warList)
		{
			final Clan clan = war.getOpposingClan(playerClan);

			packetWriter.writeD(0);//nEnemyPledgeWorldID
			packetWriter.writeD(clan.getClanId());//nEnemyPledgeSID
			packetWriter.writeSizedString(clan.getName());//sEnemyPledgeName
			packetWriter.writeSizedString(clan.getLeaderName());//sEnemyPledgeMasterName
			packetWriter.writeD(0);//nRegisterTime
		}

		return true;
	}
}