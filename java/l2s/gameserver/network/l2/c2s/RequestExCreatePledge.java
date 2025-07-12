package l2s.gameserver.network.l2.c2s;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExShowCreatePledge;
import l2s.gameserver.network.l2.s2c.PledgeShowInfoUpdatePacket;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.utils.Util;

public class RequestExCreatePledge implements IClientIncomingPacket
{
	private String clanName;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		clanName = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player==null)
			return;

		if(player.getLevel() < 10 || player.getClan() != null)
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_MEET_THE_CRITERIA_IN_ORDER_TO_CREATE_A_CLAN);
			player.sendPacket(new ExShowCreatePledge());
			return;
		}

		if(!player.canCreateClan())
		{
			player.sendPacket(SystemMsg.YOU_MUST_WAIT_10_DAYS_BEFORE_CREATING_A_NEW_CLAN);
			player.sendPacket(new ExShowCreatePledge());
			return;
		}

		if(StringUtils.isEmpty(clanName))
		{
			player.sendPacket(SystemMsg.PLEASE_CREATE_YOUR_CLAN_NAME);
			player.sendPacket(new ExShowCreatePledge());
			return;
		}

		if(clanName.length() > 16)
		{
			player.sendPacket(SystemMsg.CLAN_NAMES_LENGTH_IS_INCORRECT);
			player.sendPacket(new ExShowCreatePledge());
			return;
		}

		if(!Util.isMatchingRegexp(clanName, Config.CLAN_NAME_TEMPLATE))
		{
			player.sendPacket(SystemMsg.CLAN_NAME_IS_INVALID);
			player.sendPacket(new ExShowCreatePledge());
			return;
		}

		Clan clan = ClanTable.getInstance().createClan(player, clanName);
		if(clan == null)
		{
			player.sendPacket(SystemMsg.THIS_NAME_ALREADY_EXISTS);
			player.sendPacket(new ExShowCreatePledge());
			return;
		}

		player.sendPacket(clan.listAll());
		player.sendPacket(new PledgeShowInfoUpdatePacket(clan));
		player.updatePledgeRank();
		player.broadcastCharInfo();
	}
}