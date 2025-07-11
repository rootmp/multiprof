package l2s.gameserver.network.l2.c2s;
import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.tables.ClanTable;

public class RequestAllyInfo implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		Alliance ally = player.getAlliance();
		if (ally == null)
			return;

		int clancount = 0;
		Clan leaderclan = player.getAlliance().getLeader();
		clancount = ClanTable.getInstance().getAlliance(leaderclan.getAllyId()).getMembers().length;
		int[] online = new int[clancount + 1];
		int[] count = new int[clancount + 1];
		Clan[] clans = player.getAlliance().getMembers();
		for (int i = 0; i < clancount; i++)
		{
			online[i + 1] = clans[i].getOnlineMembers().size();
			count[i + 1] = clans[i].getAllSize();
			online[0] += online[i + 1];
			count[0] += count[i + 1];
		}

		List<IBroadcastPacket> packets = new ArrayList<IBroadcastPacket>(7 + 5 * clancount);
		packets.add(SystemMsg.ALLIANCE_INFORMATION);
		packets.add(new SystemMessage(SystemMessage.ALLIANCE_NAME_S1).addString(player.getClan().getAlliance().getAllyName()));
		packets.add(new SystemMessage(SystemMessage.CONNECTION_S1_TOTAL_S2).addNumber(online[0]).addNumber(count[0])); // Connection
		packets.add(new SystemMessage(SystemMessage.ALLIANCE_LEADER_S2_OF_S1).addString(leaderclan.getName()).addString(leaderclan.getLeaderName()));
		packets.add(new SystemMessage(SystemMessage.AFFILIATED_CLANS_TOTAL_S1_CLAN_S).addNumber(clancount)); // clan
																												// count
		packets.add(SystemMsg.CLAN_INFORMATION);
		for (int i = 0; i < clancount; i++)
		{
			packets.add(new SystemMessage(SystemMessage.CLAN_NAME_S1).addString(clans[i].getName()));
			packets.add(new SystemMessage(SystemMessage.CLAN_LEADER_S1).addString(clans[i].getLeaderName()));
			packets.add(new SystemMessage(SystemMessage.CLAN_LEVEL_S1).addNumber(clans[i].getLevel()));
			packets.add(new SystemMessage(SystemMessage.CONNECTION_S1_TOTAL_S2).addNumber(online[i + 1]).addNumber(count[i + 1]));
			packets.add(SystemMsg.LINE_500);
		}
		packets.add(SystemMsg.LINE_490);

		player.sendPacket(packets);
	}
}