package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.PledgeInfoPacket;
import l2s.gameserver.tables.ClanTable;

public class RequestPledgeInfo implements IClientIncomingPacket
{
	private int _clanId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_clanId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		if (_clanId < 10000000)
		{
			activeChar.sendActionFailed();
			return;
		}
		Clan clan = ClanTable.getInstance().getClan(_clanId);
		if (clan == null)
		{
			// Util.handleIllegalPlayerAction(activeChar, "RequestPledgeInfo[40]", "Clan
			// data for clanId " + _clanId + " is missing", 1);
			// _log.warn("Host " + client.getIpAddr() + " possibly sends fake packets.
			// activeChar: " + activeChar);
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(new PledgeInfoPacket(clan));
	}
}