package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.PledgeCrestPacket;
import l2s.gameserver.network.l2.s2c.PledgeShowInfoUpdatePacket;
import l2s.gameserver.network.l2.s2c.PledgeStatusChangedPacket;

public class RequestSetPledgeCrest implements IClientIncomingPacket
{
	private int _length;
	private byte[] _data;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_length = packet.readD();
		if(_length == CrestCache.CREST_SIZE && _length == packet.getReadableBytes())
		{
			_data = packet.readB(_length);
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(_length < 0)
		{
			activeChar.sendPacket(SystemMsg.THE_SIZE_OF_THE_UPLOADED_SYMBOL_DOES_NOT_MEET_THE_STANDARD_REQUIREMENTS);
			return;
		}

		if(_length > CrestCache.CREST_SIZE)
		{
			activeChar.sendPacket(SystemMsg.THE_SIZE_OF_THE_IMAGE_FILE_IS_INAPPROPRIATE__PLEASE_ADJUST_TO_16X12_PIXELS);
			return;
		}

		Clan clan = activeChar.getClan();

		if(clan.getLevel() < 1)
			return;
		/*if(clan.getDissolvingExpiryTime() > System.currentTimeMillis())
		{
			activeChar.sendPacket(SystemMsg.AS_YOU_ARE_CURRENTLY_SCHEDULE_FOR_CLAN_DISSOLUTION_YOU_CANNOT_REGISTER_OR_DELETE_A_CLAN_CREST);
			return;
		}*/

		if((activeChar.getClanPrivileges() & Clan.CP_CL_EDIT_CREST) != Clan.CP_CL_EDIT_CREST)
		{
			activeChar.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}

		if(clan.isPlacedForDisband())
		{
			activeChar.sendPacket(SystemMsg.AS_YOU_ARE_CURRENTLY_SCHEDULE_FOR_CLAN_DISSOLUTION_YOU_CANNOT_REGISTER_OR_DELETE_A_CLAN_CREST);
			return;
		}

		int crestId = 0;

		if(_data != null)
			crestId = CrestCache.getInstance().savePledgeCrest(clan.getClanId(), _data);
		else if(clan.hasCrest())
			CrestCache.getInstance().removePledgeCrest(clan.getClanId());

		if(crestId > 0)
		{
			clan.setCrestId(crestId);

			PledgeShowInfoUpdatePacket pu = new PledgeShowInfoUpdatePacket(clan);
			clan.broadcastToOnlineMembers(pu);

			clan.broadcastClanStatus(false, true, false);

			activeChar.sendUserInfo(true);
			PledgeStatusChangedPacket ps = new PledgeStatusChangedPacket(clan);

			activeChar.sendPacket(new PledgeCrestPacket(clan.getClanId(), crestId, _data), ps);

			activeChar.sendPacket(SystemMsg.THE_CREST_WAS_SUCCESSFULLY_REGISTERED);
		}
		else if(clan.hasCrest())
		{
			clan.setCrestId(0);
			PledgeShowInfoUpdatePacket pu = new PledgeShowInfoUpdatePacket(clan);
			clan.broadcastToOnlineMembers(pu);

			clan.broadcastClanStatus(false, true, false);
			activeChar.sendUserInfo(true);
			activeChar.sendPacket(SystemMsg.THE_CLAN_MARK_HAS_BEEN_DELETED);
		}
	}
}