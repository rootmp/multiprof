package l2s.gameserver.network.l2.c2s.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeEnemyInfoList;
import l2s.gameserver.tables.ClanTable;

/**
 * @author Eden
 */
public class RequestExPledgeEnemyInfoList implements IClientIncomingPacket
{
	private int playerClan;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		playerClan = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		final Clan clan = ClanTable.getInstance().getClan(playerClan);
		if (clan != null && clan.getAnyMember(activeChar.getObjectId()) != null)
		{
			activeChar.sendPacket(new ExPledgeEnemyInfoList(clan));
		}
	}
}