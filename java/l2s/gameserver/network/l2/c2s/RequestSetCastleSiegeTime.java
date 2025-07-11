package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.CastleSiegeInfoPacket;

/**
 * @author VISTALL
 */
public class RequestSetCastleSiegeTime implements IClientIncomingPacket
{
	private int _id, _time;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_id = packet.readD();
		_time = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, _id);
		if (castle == null)
			return;

		if (player.getClan().getCastle() != castle.getId())
			return;

		if ((player.getClanPrivileges() & Clan.CP_CS_MANAGE_SIEGE) != Clan.CP_CS_MANAGE_SIEGE)
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_SIEGE_TIME);
			return;
		}

		/*
		 * В GoD более не актуально: CastleSiegeEvent siegeEvent =
		 * castle.getSiegeEvent(); siegeEvent.setNextSiegeTime(_time);
		 */

		player.sendPacket(new CastleSiegeInfoPacket(castle, player));
	}
}