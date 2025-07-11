package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExNoticePostArrived;
import l2s.gameserver.network.l2.s2c.ExShowReceivedPostList;

/**
 * Отсылается при нажатии на кнопку "почта", "received mail" или уведомление от
 * {@link ExNoticePostArrived}, запрос входящих писем. В ответ шлется
 * {@link ExShowReceivedPostList}
 */
public class RequestExRequestReceivedPostList implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		// just a trigger
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player cha = client.getActiveChar();
		if (cha != null)
			cha.sendPacket(new ExShowReceivedPostList(cha));
	}
}