package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExGetPledgeCrestPreset;
import l2s.gameserver.tables.ClanTable;

public class RequestExGetPledgeCrestPreset implements IClientIncomingPacket
{
	private int nPledgeSId;
	@SuppressWarnings("unused")
	private int nPresetCrestDBID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nPledgeSId = packet.readD();
		nPresetCrestDBID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		Clan clan = ClanTable.getInstance().getClan(nPledgeSId);
		if(clan == null)
			return;
		player.sendPacket(new ExGetPledgeCrestPreset(clan));
	}
}