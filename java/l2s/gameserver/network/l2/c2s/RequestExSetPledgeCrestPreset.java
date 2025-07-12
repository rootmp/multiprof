package l2s.gameserver.network.l2.c2s;

import java.nio.ByteBuffer;

import l2s.commons.network.PacketReader;
import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.PledgeStatusChangedPacket;

public class RequestExSetPledgeCrestPreset implements IClientIncomingPacket
{
	private int nType;
	private int nPresetCrestDBID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nType = packet.readD();
		nPresetCrestDBID = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null || player.getClan() == null)
			return;

		Clan clan = player.getClan();

		if(clan.getLevel() < 1)
			return;

		if((player.getClanPrivileges() & Clan.CP_CL_EDIT_CREST) != Clan.CP_CL_EDIT_CREST)
		{
			player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		if(nPresetCrestDBID > 0)
		{
			if(nType == 0)
			{
				clan.setCrestId(0);
				CrestCache.getInstance().removePledgeCrest(clan.getClanId());
			}
			else
			{
				clan.setCrestId(nPresetCrestDBID);
				byte[] crestIdBytes = ByteBuffer.allocate(4).putInt(nPresetCrestDBID).array();
				CrestCache.getInstance().savePledgeCrest(clan.getClanId(), crestIdBytes);
			}

			PledgeStatusChangedPacket ps = new PledgeStatusChangedPacket(clan);
			for(Player member : clan.getOnlineMembers())
			{
				member.sendPacket(ps);
				member.broadcastUserInfo(true);
			}
			clan.broadcastClanStatus(false, true, false);
			player.sendPacket(SystemMsg.THE_CREST_WAS_SUCCESSFULLY_REGISTERED);
		}
	}
}