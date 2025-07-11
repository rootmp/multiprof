package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;

public class RequestExPledgeMercenaryMemberJoin implements IClientIncomingPacket
{
	private int charObjectId;
	private boolean join;
	private int castleId;
	private int clanId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		charObjectId = packet.readD();
		join = packet.readD() > 0;
		castleId = packet.readD();
		clanId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null || activeChar.getObjectId() != charObjectId)
			return;

		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, castleId);
		if (castle == null)
			return;

		activeChar.sendPacket(SystemMsg.YOU_CANNOT_APPLY_FOR_MERCENARY_NOW);
		// TODO: Impl mercenary system.
	}
}
