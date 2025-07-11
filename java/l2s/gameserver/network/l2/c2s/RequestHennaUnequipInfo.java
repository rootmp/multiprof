package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.HennaUnequipInfoPacket;
import l2s.gameserver.templates.henna.HennaTemplate;

public class RequestHennaUnequipInfo implements IClientIncomingPacket
{
	private int _symbolId;

	/**
	 * format: d
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_symbolId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		HennaTemplate henna = HennaHolder.getInstance().getHenna(_symbolId);
		if (henna != null)
			player.sendPacket(new HennaUnequipInfoPacket(henna, player));
	}
}