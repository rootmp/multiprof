package l2s.gameserver.network.l2.c2s;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.ClanWar;
import l2s.gameserver.model.pledge.ClanWar.ClanWarPeriod;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.tables.ClanTable;

/**
 * @author GodWorld & reworked by Bonux
 **/
public final class RequestSurrenderPledgeWar implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestSurrenderPledgeWar.class);

	private String _pledgeName;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_pledgeName = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Clan clan = activeChar.getClan();
		if (clan == null)
			return;

		Clan targetClan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (targetClan == null)
		{
			activeChar.sendPacket(SystemMsg.THE_TARGET_FOR_DECLARATION_IS_WRONG);
			activeChar.sendActionFailed();
			return;
		}

		_log.info(getClass().getSimpleName() + ": by " + clan.getName() + " with " + _pledgeName);

		if (!clan.isAtWarWith(targetClan.getClanId()))
		{
			// TODO: activeChar.sendMessage("You aren't at war with this clan.");
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_HAVE_SURRENDERED_TO_THE_S1_CLAN).addString(_pledgeName));
		// activeChar.deathPenalty(false, false, false);

		ClanWar war = clan.getWarWith(targetClan.getClanId());
		if (war != null)
			war.setPeriod(ClanWarPeriod.PEACE);
	}
}