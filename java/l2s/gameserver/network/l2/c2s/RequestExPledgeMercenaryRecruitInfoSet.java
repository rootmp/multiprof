package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.SystemMsg;

public class RequestExPledgeMercenaryRecruitInfoSet implements IClientIncomingPacket
{
	private int castleId;
	private int unk1;
	private int unk2;
	private long rewardRate;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		castleId = packet.readD();
		unk1 = packet.readD();
		unk2 = packet.readD();
		rewardRate = packet.readQ();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, castleId);
		if (castle == null)
			return;

		if (castle.getId() != 3 && castle.getId() != 7)
		{
			activeChar.sendPacket(new CustomMessage("l2s.gameserver.model.entity.siege.Siege.NoMercenariesHired"));
			return;
		}

		CastleSiegeEvent siegeEvent = castle.getSiegeEvent();
		if (siegeEvent == null)
			return;

		Clan playerClan = activeChar.getClan();
		if (playerClan == null)
		{
			activeChar.sendPacket(SystemMsg.TO_RECRUIT_MERCENARIES_CLANS_MUST_PARTICIPATE_IN_THE_CASTLE_SIEGE_);
			return;
		}

		// if (!activeChar.hasPrivilege(Privilege.CS_FS_SIEGE_WAR)) {
		activeChar.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
		// return;
		// }

		// TODO: Impl mercenary system.
	}
}
