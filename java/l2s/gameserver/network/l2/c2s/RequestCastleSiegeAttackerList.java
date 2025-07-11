package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Residence;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.CastleSiegeAttackerListPacket;

public class RequestCastleSiegeAttackerList implements IClientIncomingPacket
{
	private int _unitId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unitId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		Residence residence = ResidenceHolder.getInstance().getResidence(_unitId);
		if (residence != null)
			client.sendPacket(new CastleSiegeAttackerListPacket(residence));
	}
}